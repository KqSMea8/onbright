package com.bright.apollo.session;

import com.bright.apollo.bean.PushMessage;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.PushMessageType;
import com.bright.apollo.listener.SessionCloseListener;
import com.bright.apollo.service.OboxService;
import com.zz.common.util.StringUtils;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private static final Logger log = Logger.getLogger(SessionManager.class);

    private Map<Integer, ClientSession> preAuthenticatedSessions = new ConcurrentHashMap<Integer, ClientSession>();

    private Map<Integer, String> sessionKeyMap = new ConcurrentHashMap<Integer, String>();

    private Map<String, ClientSession> authenticatedSessions = new ConcurrentHashMap<String, ClientSession>();

    private Map<String, ClientSession> uidSession = new ConcurrentHashMap<String, ClientSession>();

    private Map<String, Set<ClientSession>> multipleUidSession =new ConcurrentHashMap<String, Set<ClientSession>>();

    private ClientSessionListener sessionListener = new ClientSessionListener();

    @Autowired
    private PushObserverManager pushObserverManager;

    private String SESSION_KEY_SPRATE = "#";

    @Autowired
    private OboxService oboxService;

    private static SessionManager instance;

    public static SessionManager getInstance() {
        if (instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * 创建session
     * @return
     */
    public ClientSession createSession(Channel  channel) {
        ClientSession preClientSession = new ClientSession(channel);
        preAuthenticatedSessions.put(channel.hashCode(), preClientSession);

        preClientSession.registerCloseListener(sessionListener);

        return preClientSession;
    }

    /**
     * 连接通道的unique id
     * @param uid
     * @param appKey
     * @return
     */
    public String sessionKey(String uid, String appKey) {
        if(!StringUtils.isEmpty(appKey))
            return appKey + SESSION_KEY_SPRATE + uid;
        return "" + SESSION_KEY_SPRATE + uid;
    }

    /**
     * 保存已经验证的session
     * @param clientSession
     */
    public void addSession(ClientSession clientSession) {
        preAuthenticatedSessions.remove(clientSession.getChannel().hashCode());

        authenticatedSessions.put(sessionKey(clientSession.getUid(), clientSession.getAppKey()), clientSession);
        uidSession.put(clientSession.getUid(), clientSession);
        Set<ClientSession> set = multipleUidSession.get(clientSession.getUid());
        if(set==null||set.isEmpty()||set.size()==0)
            set=new HashSet<ClientSession>();
        set.add(clientSession);
        multipleUidSession.put(clientSession.getUid(), set);
        sessionKeyMap.put(clientSession.getChannel().hashCode(), sessionKey(clientSession.getUid(), clientSession.getAppKey()));
    }



    /**
     * 通过uid & appkey获取session
     * @return
     */
    public ClientSession getClientSessionBySessionKey(String sessionKey) {
        return authenticatedSessions.get(sessionKey);
    }
    public ClientSession getClientSession(String uid) {
        return authenticatedSessions.get(sessionKey(uid, ""));
    }
    public ClientSession getClientSessionByUid(String uid) {
        return uidSession.get(uid);
    }
    public Set<ClientSession> getSetClientSessionByUid(String uid) {
        return multipleUidSession.get(uid);
    }
    /**
     * 获取session
     * @return
     */
    public ClientSession getClientSession(Channel channel) {
        int hascode = channel.hashCode();
        ClientSession clientSession = preAuthenticatedSessions.get(hascode);
        if (clientSession != null) {
            return clientSession;
        }
        String sessionKey = sessionKeyMap.get(hascode);
        return authenticatedSessions.get(sessionKey);
    }
    /**
     * 删除session
     */
    public void removeClientSession(ClientSession clientSession) {
        log.info("===uid===:"+clientSession.getUid());
        if(!StringUtils.isEmpty(clientSession.getAppKey())){
            log.info("===appkey===:"+clientSession.getAppKey());
        }
        preAuthenticatedSessions.remove(clientSession.getChannel().hashCode());

        if (clientSession.getStatus() != Session.STATUS_CONNECTED) {
            sessionKeyMap.remove(clientSession.getChannel().hashCode());
            authenticatedSessions.remove(sessionKey(clientSession.getUid(), clientSession.getAppKey()));
        }
        if (!StringUtils.isEmpty(clientSession.getUid())) {
            uidSession.remove(clientSession.getUid());
            multipleUidSession.remove(clientSession.getUid());
        }

    }

    private class ClientSessionListener implements SessionCloseListener {

        @Override
        public void onSessionClose(Object handback) {
            try {
                log.info("====ClientSessionListener onSessionClose===");
                ClientSession clientSession = (ClientSession) handback;

                TObox obox = oboxService.queryOboxsByOboxSerialId(clientSession.getUid());
//                OboxBusiness.queryOboxsByOboxSerialId();
                if(obox!=null){
                    log.info("====ClientSessionListener UserIdSet===:"+clientSession.getUserIdSet());
                    log.info("====ClientSessionListener obox===:"+obox.getOboxSerialId());
                    if(!clientSession.getUserIdSet().isEmpty()){
                        Set<String> userIdSet = clientSession.getUserIdSet();
                        PushMessage pushMessage=new PushMessage();
                        pushMessage.setType(PushMessageType.OBOX_ONLINE.getValue());
                        pushMessage.setSerialId(clientSession.getUid());
                        pushMessage.setOnLine(false);
                        Iterator<String> it = userIdSet.iterator();
                        while (it.hasNext()) {
                            log.info("====ClientSessionListener userIdSet===");
                            String key = it.next();
                            log.info("===clientsession key===:"+key);
                            ClientSession pushSession =  SessionManager.getInstance()
                                    .getClientSessionBySessionKey(key);
                            pushObserverManager.sendMessage(pushMessage, pushSession);
                        }
                    }
                    clientSession.getUserIdSet().clear();
                    obox.setOboxStatus(0);
                    oboxService.update(obox);
//                    OboxBusiness.updateObox(obox);
                }else {
                    log.info("====ClientSessionListener obox is null===");
                }
                removeClientSession(clientSession);
                if (clientSession.getStatus() != Session.STATUS_CLOSED) {

                }
                log.info("close session, close type : " + clientSession.getCloseEnum());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {

            }
        }
    }
}
