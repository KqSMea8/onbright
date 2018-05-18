package com.bright.apollo.session;

import com.bright.apollo.bean.Message;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.handler.BasicHandler;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PushThreadPool {
    private static ExecutorService executor;


    private static final Logger log = Logger.getLogger(PushThreadPool.class);

    @Autowired
    private PushUtil pushUtil;

    public PushThreadPool() {
        executor = Executors.newCachedThreadPool();
    }

    public void pushCmd(BasicHandler filterCMDHandler,
                        ClientSession clientSession, Message<String> msg) {
        executor.submit(new FilterCMD(filterCMDHandler, clientSession, msg));
    }

    public void pushHttp(RequestParam requestParam, CMDEnum cmdEnum,
                         String accessToken, JsonObject respJsonObject) {
        executor.submit(new HttpInter(requestParam, cmdEnum, accessToken,
                respJsonObject));
    }

    static class FilterCMD implements Runnable {
        private BasicHandler filterCMDHandler;
        private ClientSession clientSession;
        private Message<String> msg;

        public FilterCMD(BasicHandler filterCMDHandler,
                         ClientSession clientSession, Message<String> msg) {
            this.clientSession = clientSession;
            this.filterCMDHandler = filterCMDHandler;
            this.msg=msg;
        }

        @Override
        public void run() {
            try {
                filterCMDHandler.process(clientSession, msg);
            } catch (Exception e) {
                log.error("===filterCMDHandler.process error===", e);
            }
        }
    }

    class HttpInter implements Runnable {
        private RequestParam requestParam;
        private CMDEnum cmdEnum;
        private String accessToken;
        private JsonObject respJsonObject;

        public HttpInter(RequestParam requestParam, CMDEnum cmdEnum,
                         String accessToken, JsonObject respJsonObject) {
            this.requestParam = requestParam;
            this.cmdEnum = cmdEnum;
            this.accessToken = accessToken;
            this.respJsonObject = respJsonObject;
        }

        @Override
        public void run() {
            try {
                pushUtil.filterForwordSeverChange(requestParam, cmdEnum,
                        accessToken, respJsonObject);
            } catch (Exception e) {
                log.error("===filterCMDHandler.process error===", e);
            }
        }
    }
}
