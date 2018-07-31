package com.bright.apollo.listener;



import com.bright.apollo.socket.MNServer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ContextListener implements ApplicationListener<ContextRefreshedEvent> {

    Logger  logger = Logger.getLogger(ContextListener.class);

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
    @Autowired
    private MNServer mnserver;
//
//    private static Thread mnsThread;
//
//    @Autowired
//    private SceneService sceneService;




    @Override
    @Async("myExecutor")
    public void onApplicationEvent(ContextRefreshedEvent applicationEvent) {

//        WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext())
//                .getAutowireCapableBeanFactory().autowireBean(this);
        logger.info(" ====== ContextListener ====== ");
        try {
//            //加载离线消息
//            List<TScene> tScenes = sceneService.getALlScene();
//            for (TScene tScene : tScenes) {
//                tScene.setSceneRun(0);
//                sceneService.updateScene(tScene);
////                SceneBusiness.updateScene(tScene);
//            }

//            executor = Executors
//                    .newFixedThreadPool(9);

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

//            mnserver = new MNServer(AliRegionEnum.SOURTHCHINA);
//        	mnsThread = new Thread(mnserver);
//        	mnsThread.start();

//            MNServer mnserver2 = new MNServer(AliRegionEnum.AMERICA);
//        	new Thread(mnserver2).start();

            //executor.submit(nettyThread);
            //executor.submit(pushThread);
            new Thread(mnserver).start();
//            executor.submit(new Thread(mnserver2));
//            MNService mnService = MNService.getInstance(AliRegionEnum.SOURTHCHINA);
//            System.out.println("------mnService ------"+mnService);

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
