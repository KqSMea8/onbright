package com.bright.apollo.listener;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.service.SceneService;
import com.bright.apollo.socket.MNServer;
import com.zz.common.log.LogService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContextListener implements ApplicationListener<ApplicationStartingEvent> {

    Logger  logger = Logger.getLogger(ContextListener.class);

    private static ExecutorService executor;
//
//    //private static Thread nettyThread;
//
//    private static Thread pushThread;
//
//    private static Thread keyCodeThread;
//
//
////    private static PushServer nettyPush;
//
////    private static KeyCodePushServer keyCodePush;
//
    private static MNServer mnserver;
//
//    private static Thread mnsThread;
//
//    @Autowired
//    private SceneService sceneService;



    @Override
    public void onApplicationEvent(ApplicationStartingEvent applicationEvent) {

//        WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext())
//                .getAutowireCapableBeanFactory().autowireBean(this);
        logger.info("1231231231231231313123");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=");

        try {
//            //加载离线消息
//            List<TScene> tScenes = sceneService.getALlScene();
//            for (TScene tScene : tScenes) {
//                tScene.setSceneRun(0);
//                sceneService.updateScene(tScene);
////                SceneBusiness.updateScene(tScene);
//            }

            executor = Executors
                    .newFixedThreadPool(9);

            //nettyServer = new Server();
//            nettyPush=new PushServer();

            //nettyThread = new Thread(nettyServer);
//            pushThread = new Thread(nettyPush);
//        	nettyThread.start();
//            pushThread.start();

            //遥控测试码推送
//            keyCodePush = new KeyCodePushServer();
//            keyCodeThread = new Thread(keyCodePush);
//            keyCodeThread.start();

            mnserver = new MNServer(AliRegionEnum.SOURTHCHINA);
//        	mnsThread = new Thread(mnserver);
//        	mnsThread.start();

//            MNServer mnserver2 = new MNServer(AliRegionEnum.AMERICA);
//        	new Thread(mnserver2).start();

            //executor.submit(nettyThread);
            //executor.submit(pushThread);
            executor.submit(new Thread(mnserver));
//            executor.submit(new Thread(mnserver2));

            logger.info("OBOX server starting ");
//            LogService.info("OBOX server starting ");
        } catch (Exception e) {
            logger.info(e.getMessage());
//            LogService.info(e.getMessage());
        }

    }
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
////        LogService.info("OBOX server shundown ");
//        logger.info("OBOX server shundown ");
//        if (executor != null) {
//            executor.shutdown();
//        }
//    }
}
