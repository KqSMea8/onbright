package com.bright.apollo.service;

import com.bright.apollo.cache.CMDMsgCache;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.Command;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.session.SessionManager;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.EncDecHelper;
import com.zz.common.util.ByteUtils;
import com.zz.common.util.PropertiesUtils;
import com.zz.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CMDMessageService {

    private static Logger log = Logger.getLogger(CMDMessageService.class);

    private static final int packageLength = 64;

    private static final String head = "32303131";

    private static final String orgHead = "38383838";

    private static final String upgradeHead = "32313131";

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private CMDMsgCache cmdMsgCache;

    @Autowired
    private EncDecHelper helper;

    public OboxResp send(TObox obox, CMDEnum cmd, byte [] data, long max_waitting_time, int RFControl) {

        ClientSession clientSession = sessionManager.getClientSession(obox.getOboxSerialId());
        if (clientSession == null) {
            log.info(String.format("obox_serial_id %s isn't online!", obox.getOboxSerialId()));
            return new OboxResp(OboxResp.Type.not_online);
        }

        byte [] out = new byte [packageLength];

        out[0] = 0x00;
        out[1] = 0x00;
        out[2] = (byte)RFControl;
        out[3] = 0x00;

        // 命令2个字节
        byte [] cmdBytes = ByteHelper.hexStringToBytes(cmd.getSendCMD());
        System.arraycopy(cmdBytes, 0, out, 4, cmdBytes.length);

        // 真实数据长度
        out[6] = (byte)data.length;
        // 真实数据
        System.arraycopy(data, 0, out, 7, data.length);
        // 结束符
        out[61] = (byte)Integer.parseInt("55", 16);
        // 计算校验和
        int sum = 0;
        for (int i = 0; i < 62; i ++) {
            int v = out[i] & 0xFF;
            sum += v;
        }

        byte [] sumBytes = ByteUtils.convertInt2Bytes(sum);
        out [62] = sumBytes[2];
        out [63] = sumBytes[3];

        log.info("send obox:"+obox.getOboxSerialId() + " : " + ByteHelper.bytesToHexString(out));


        byte [] decodeOut = new byte [68];

        if (clientSession.getAES() == 1) {
//            byte [] pwdBytes = ByteHelper.stringToAsciiBytes(obox.getOboxPwd(), 8);
//            byte [] randomByte = ByteHelper.hexStringToBytes(clientSession.getRandom());
//            helper.OB_DATA_ENC_DEC(out, randomByte, pwdBytes, 0, 1);

            byte [] headByte = ByteHelper.hexStringToBytes(head);
            System.arraycopy(headByte, 0, decodeOut, 0, headByte.length);
            System.arraycopy(out, 0, decodeOut, headByte.length, packageLength);
        }else {
            byte [] headByte = ByteHelper.hexStringToBytes(orgHead);
            System.arraycopy(headByte, 0, decodeOut, 0, headByte.length);
            System.arraycopy(out, 0, decodeOut, headByte.length, packageLength);
        }

        if (!clientSession.process(decodeOut)) {
            return new OboxResp(OboxResp.Type.socket_write_error);
        }

        Reply(obox, cmd);

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < max_waitting_time) {
            try {
                String reply = cmdMsgCache.getReply(obox.getOboxSerialId(), cmd.getReplyCMD());
                if (!StringUtils.isEmpty(reply)) {
                    if ("00".equals(reply)) {
                        return new OboxResp(OboxResp.Type.obox_process_failure, reply);
                    }
                    return new OboxResp(OboxResp.Type.success, reply);
                }

                reply = cmdMsgCache.getReply(obox.getOboxSerialId(), Command.ERROR.getValue());
                if (!StringUtils.isEmpty(reply)) {
                    return new OboxResp(OboxResp.Type.reply_timeout, reply);
                }

                TimeUnit.MILLISECONDS.sleep(150);
            } catch (Exception e) {

            }
        }
        return new OboxResp(OboxResp.Type.reply_timeout);
    }

    public List<String> sendWait(TObox obox, CMDEnum cmd, byte [] data) {
        ClientSession clientSession = sessionManager.getClientSession(obox.getOboxSerialId());
        List<String> replyList = new ArrayList<String>();
        if (clientSession == null) {
            log.info(String.format("obox_serial_id %s isn't online!", obox.getOboxSerialId()));
            return replyList;
        }
        byte [] out = new byte [packageLength];
        //秘钥4个字节
        out[0] = 0x00;
        out[1] = 0x00;
        out[2] = 0x00;
        out[3] = 0x00;

        // 命令2个字节
        byte [] cmdBytes = ByteHelper.hexStringToBytes(cmd.getSendCMD());
        System.arraycopy(cmdBytes, 0, out, 4, cmdBytes.length);

        // 真实数据长度
        out[6] = (byte)data.length;
        // 真实数据
        System.arraycopy(data, 0, out, 7, data.length);
        // 结束符
        out[61] = (byte)Integer.parseInt("55", 16);
        // 计算校验和
        int sum = 0;
        for (int i = 0; i < 62; i ++) {
            int v = out[i] & 0xFF;
            sum += v;
        }
        byte [] sumBytes = ByteUtils.convertInt2Bytes(sum);
        out [62] = sumBytes[2];
        out [63] = sumBytes[3];

        log.info("send obox:"+obox.getOboxSerialId() + " : " + ByteHelper.bytesToHexString(out));

        byte [] decodeOut = new byte [68];

        if (clientSession.getAES() == 1) {
//            byte [] pwdBytes = ByteHelper.stringToAsciiBytes(obox.getOboxPwd(), 8);
//            byte [] randomByte = ByteHelper.hexStringToBytes(clientSession.getRandom());
//            helper.OB_DATA_ENC_DEC(out, randomByte, pwdBytes, 0, 1);

            byte [] headByte = ByteHelper.hexStringToBytes(head);
            System.arraycopy(headByte, 0, decodeOut, 0, headByte.length);
            System.arraycopy(out, 0, decodeOut, headByte.length, packageLength);
        }else {
            byte [] headByte = ByteHelper.hexStringToBytes(orgHead);
            System.arraycopy(headByte, 0, decodeOut, 0, headByte.length);
            System.arraycopy(out, 0, decodeOut, headByte.length, packageLength);
        }

        if (!clientSession.process(decodeOut)) {
            return replyList;
        }

        Reply(obox, cmd);

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < PropertiesUtils.getLong("push_reply_timeout", 1500)) {
            try {
                String reply = cmdMsgCache.getReply(obox.getOboxSerialId(), cmd.getReplyCMD());
                if (!StringUtils.isEmpty(reply)) {
                    if ("00".equals(reply)) {

                    }else {
                        replyList.add(reply);
                    }
                }else {
                    TimeUnit.MILLISECONDS.sleep(150);
                }

            } catch (Exception e) {

            }
        }
        return replyList;
    }

    public OboxResp send(TObox obox, CMDEnum cmd, byte [] data) {
        return send(obox, cmd, data, PropertiesUtils.getLong("push_reply_timeout", 15000),0);
    }

    public OboxResp setSend(TObox obox, CMDEnum cmd, byte [] data) {
        return send(obox, cmd, data, PropertiesUtils.getLong("push_reply_timeout", 25000),0);
    }

    public List<String> searchReply(TObox obox) {
        List<String> replyList = new ArrayList<String>();

        for (int i = 1; i < 256; i++) {
            try {
                String reply = cmdMsgCache.getSearch(obox.getOboxSerialId(),String.valueOf(i));
                if (!StringUtils.isEmpty(reply)) {
                    if ("00".equals(reply)) {
                        continue;
                    }else {
                        replyList.add(reply);
                    }

                }else{
                    continue;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return replyList;
            }
        }
        return replyList;
    }

    public String Reply(TObox obox, CMDEnum cmd) {

        try {
            String reply = cmdMsgCache.getReply(obox.getOboxSerialId(), cmd.getReplyCMD());
            if (!StringUtils.isEmpty(reply)) {
                if ("00".equals(reply)) {
                    return null;
                }else {
                    return reply;
                }

            }else{
                reply = cmdMsgCache.getReply(obox.getOboxSerialId(), Command.ERROR.getValue());
                if (!StringUtils.isEmpty(reply)) {
                    return reply;
                }else {
                    return null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public OboxResp sendToSession(ClientSession clientSession, TObox obox, CMDEnum cmd, byte [] data, long max_waitting_time,int RFControl) {

        if (clientSession == null) {
            log.info(String.format("client session is null!", obox.getOboxSerialId()));
            return new OboxResp(OboxResp.Type.not_online);
        }
        byte [] out = new byte [packageLength];

        out[0] = 0x00;
        out[1] = 0x00;
        out[2] = (byte)RFControl;
        out[3] = 0x00;

        // 命令2个字节
        byte [] cmdBytes = ByteHelper.hexStringToBytes(cmd.getSendCMD());
        System.arraycopy(cmdBytes, 0, out, 4, cmdBytes.length);

        // 真实数据长度
        out[6] = (byte)data.length;
        // 真实数据
        System.arraycopy(data, 0, out, 7, data.length);
        // 结束符
        out[61] = (byte)Integer.parseInt("55", 16);
        // 计算校验和
        int sum = 0;
        for (int i = 0; i < 62; i ++) {
            int v = out[i] & 0xFF;
            sum += v;
        }
        byte [] sumBytes = ByteUtils.convertInt2Bytes(sum);
        out [62] = sumBytes[2];
        out [63] = sumBytes[3];

        log.info("send obox:"+obox.getOboxSerialId() + " : " + ByteHelper.bytesToHexString(out));

        byte [] decodeOut = new byte [68];

        if (clientSession.getAES() == 1) {
//            byte [] pwdBytes = ByteHelper.stringToAsciiBytes(obox.getOboxPwd(), 8);
//            byte [] randomByte = ByteHelper.hexStringToBytes(clientSession.getRandom());
//            helper.OB_DATA_ENC_DEC(out, randomByte, pwdBytes, 0, 1);

            byte [] headByte = ByteHelper.hexStringToBytes(head);
            System.arraycopy(headByte, 0, decodeOut, 0, headByte.length);
            System.arraycopy(out, 0, decodeOut, headByte.length, packageLength);
        }else {
            byte [] headByte = ByteHelper.hexStringToBytes(orgHead);
            System.arraycopy(headByte, 0, decodeOut, 0, headByte.length);
            System.arraycopy(out, 0, decodeOut, headByte.length, packageLength);
        }

        if (!clientSession.process(decodeOut)) {
            return new OboxResp(OboxResp.Type.socket_write_error);
        }

        Reply(obox, cmd);

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < max_waitting_time) {
            try {
                String reply = cmdMsgCache.getReply(obox.getOboxSerialId(), cmd.getReplyCMD());
                if (!StringUtils.isEmpty(reply)) {
                    if ("00".equals(reply)) {
                        return new OboxResp(OboxResp.Type.obox_process_failure, reply);
                    }
                    return new OboxResp(OboxResp.Type.success, reply);
                }

                reply = cmdMsgCache.getReply(obox.getOboxSerialId(), Command.ERROR.getValue());
                if (!StringUtils.isEmpty(reply)) {
                    return new OboxResp(OboxResp.Type.reply_timeout, reply);
                }

                TimeUnit.MILLISECONDS.sleep(15);
            } catch (Exception e) {

            }
        }
        return new OboxResp(OboxResp.Type.reply_timeout);
    }

    public OboxResp sendToSession(ClientSession clientSession,TObox obox, CMDEnum cmd, byte [] data) {
        return sendToSession(clientSession, obox, cmd, data, 0,0);
    }

    public OboxResp upgrade(TObox obox, CMDEnum cmd, byte [] data,long crc, long max_waitting_time) {

        ClientSession clientSession = sessionManager.getClientSession(obox.getOboxSerialId());
        if (clientSession == null) {
            log.info(String.format("obox_serial_id %s isn't online!", obox.getOboxSerialId()));
            return new OboxResp(OboxResp.Type.not_online);
        }
        byte [] out = new byte [packageLength];

        // 命令1个字节
        byte [] cmdBytes = ByteHelper.hexStringToBytes(cmd.getSendCMD());
        System.arraycopy(cmdBytes, 0, out, 0, cmdBytes.length);

        out[1] = (byte)((crc >> 8) & 0x00ff);
        out[2] = (byte)(crc & 0x00ff);
        out[3] = (byte)0x00;

        // 真实数据
        System.arraycopy(data, 0, out, 4, data.length);

        // 计算校验和
        int sum = 0;
        for (int i = 0; i < 62; i ++) {
            int v = out[i] & 0xFF;
            sum += v;
        }
        byte [] sumBytes = ByteUtils.convertInt2Bytes(sum);
        out [62] = sumBytes[2];
        out [63] = sumBytes[3];

        log.info("send obox:"+obox.getOboxSerialId() + " : " + ByteHelper.bytesToHexString(out));

//        if (clientSession.getAES() == 1) {
//            byte [] pwdBytes = ByteHelper.stringToAsciiBytes(obox.getOboxPwd(), 8);
//            byte [] randomByte = ByteHelper.hexStringToBytes(clientSession.getRandom());
//            helper.OB_DATA_ENC_DEC(out, randomByte, pwdBytes, 0, 1);
//        }

        byte [] decodeOut = new byte [68];
        byte [] headByte = ByteHelper.hexStringToBytes(upgradeHead);
        System.arraycopy(headByte, 0, decodeOut, 0, headByte.length);
        System.arraycopy(out, 0, decodeOut, headByte.length, packageLength);

        if (!clientSession.process(decodeOut)) {
            return new OboxResp(OboxResp.Type.socket_write_error);
        }

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < max_waitting_time) {
            try {
                String reply = cmdMsgCache.getReply(obox.getOboxSerialId(), cmd.getReplyCMD());
                if (!StringUtils.isEmpty(reply)) {
                    return new OboxResp(OboxResp.Type.success, reply);
                }

                TimeUnit.MILLISECONDS.sleep(15);
            } catch (Exception e) {

            }
        }
        return new OboxResp(OboxResp.Type.reply_timeout);
    }

//    public static OboxResp sendToCamera(ClientSession clientSession, TCamera camera, CMDEnum cmd, byte [] data, long max_waitting_time) {
//
//        if (clientSession == null) {
//            log.info(String.format("client session is null!", camera.getMacAddr()));
//            return new OboxResp(Type.not_online);
//        }
//        byte [] out = new byte [data.length + 1];
//        //秘钥4个字节
//
//        // 命令2个字节
//        byte [] cmdBytes = ByteHelper.hexStringToBytes(cmd.getSendCMD());
//        System.arraycopy(cmdBytes, 0, out, 0, cmdBytes.length);
//
//        // 真实数据
//        System.arraycopy(data, 0, out, 1, data.length);
//        // 结束符
//
//
////		log.info("Send before encode :" + ByteHelper.bytesToHexString(out));
//
////		byte [] pwdBytes = ByteHelper.stringToAsciiBytes(obox.getOboxPwd(), 8);
////		byte [] randomByte = ByteHelper.hexStringToBytes(clientSession.getRandom());
////		helper.OB_DATA_ENC_DEC(out, randomByte, pwdBytes, 0, 1);
//
//        byte [] decodeOut = new byte [out.length + 4 + 1];
//        byte [] headByte = ByteHelper.hexStringToBytes(head);
//        System.arraycopy(headByte, 0, decodeOut, 0, headByte.length);
//        decodeOut[4] = (byte)data.length;
//        System.arraycopy(out, 0, decodeOut, headByte.length+1, data.length);
//
//        if (!clientSession.process(decodeOut)) {
//            return new OboxResp(Type.socket_write_error);
//        }
//
////
//        long startTime = System.currentTimeMillis();
//        while (System.currentTimeMillis() - startTime < max_waitting_time) {
//            try {
//                String reply = CMDMsgCache.getReply(camera.getMacAddr(), cmd.getReplyCMD());
//                if (!StringUtils.isEmpty(reply)) {
//                    if ("00".equals(reply)) {
//                        return new OboxResp(Type.obox_process_failure, reply);
//                    }
//                    return new OboxResp(Type.success, reply);
//                }
//
//                TimeUnit.MILLISECONDS.sleep(15);
//            } catch (Exception e) {
//
//            }
//        }
//        return new OboxResp(Type.reply_timeout);
//    }
}
