package com.bright.apollo.session;

import com.alibaba.fastjson.JSON;
import com.bright.apollo.bean.Message;
import com.bright.apollo.bean.PushMessage;
import com.bright.apollo.listener.SessionCloseListener;
import com.bright.apollo.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import io.netty.channel.Channel;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class ClientSession implements Session {

    private Logger log = Logger
            .getLogger(ClientSession.class);

    private Channel channel;

    private String uid;

    private String random = "000102030405060708090a0b0c0d0e0f";

    private String appKey;

    private volatile int AES = 1;

    private volatile boolean closed = false;

    private volatile int avaibleByte = 0x00;

    protected int status = Session.STATUS_CONNECTED;

    private SessionCloseListener closeListener;

    private CloseEnum closeEnum;

    private Set<String> userIdSet=new ConcurrentSkipListSet<String>();

    public ClientSession(Channel channel) {
        this.channel = channel;
    }

    public ClientSession() {

    }

    public Set<String> getUserIdSet() {
        return userIdSet;
    }

    public void setUserIdSet(Set<String> userIdSet) {
        this.userIdSet = userIdSet;
    }

    public int getAES() {
        return AES;
    }

    public void setAES(int aES) {
        AES = aES;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAvaibleByte() {
        return avaibleByte;
    }

    public void setAvaibleByte(int avaibleByte) {
        this.avaibleByte = avaibleByte;
    }

    @Override
    public void close(CloseEnum closeEnum) {
        if (closed) {
            return ;
        }
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {

            }
            closed = true;
            this.closeEnum = closeEnum;
        }
        notifyCloseListeners();
        userIdSet.clear();
        setStatus(Session.STATUS_CLOSED);
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    /**
     * 消息发送
     *
     * @param msg
     */
    @Override
    public boolean process(Message<String> msg) {
        try {
            if (msg != null && channel.isActive() && channel.isWritable()) {
                ByteBufAllocator byteBufAllocator = channel.alloc();
                ByteBuf out = byteBufAllocator.directBuffer();

                if (msg.getData() != null && msg.getData().length() > 0) {
                    out.writeBytes(msg.getData().getBytes(CharsetUtil.UTF_8));
                }
                out.writeBytes(Constants.SOCKET_SPLIT);
                channel.writeAndFlush(out);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 消息发送
     */
    public boolean process(final PushMessage msg) {
        try {
            if (msg != null && channel.isActive() && channel.isWritable()) {
                channel.writeAndFlush("STR"+JSON.toJSONString(msg)+"END").sync();
                return true;
            } else {
                log.info("===channel can not use===");
                log.info("===clientsession appkey===:" + getAppKey());
                log.info("===clientsession uid===:" + getUid());
                log.info("===clientsession channel===:"
                        + getChannel().hashCode());
            }
        } catch (Exception e) {
            log.error("channel  error "+e.getMessage());
        }
        return false;
    }

    @Override
    public boolean process(byte[] bs) {
        try {
            if (bs != null && channel.isActive() && channel.isWritable()) {
                ByteBufAllocator byteBufAllocator = channel.alloc();
                ByteBuf out = byteBufAllocator.directBuffer();
                out.writeBytes(bs);
                channel.writeAndFlush(out);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public CloseEnum getCloseEnum() {
        return closeEnum;
    }

    /**
     * 注册连接关闭监听器
     * @param closeListener
     */
    public void registerCloseListener(SessionCloseListener closeListener) {
        if (this.closeListener != null) {
            throw new IllegalStateException("Close listener already configured");
        }
        if (isClosed()) {
            notifyCloseListeners();
        }
        else {
            this.closeListener = closeListener;
        }
    }

    /**
     * 连接关闭通知监听器
     */
    private void notifyCloseListeners() {
        if (closeListener != null) {
            closeListener.onSessionClose(this);
        }
    }

}
