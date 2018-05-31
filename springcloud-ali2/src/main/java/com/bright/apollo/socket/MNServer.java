package com.bright.apollo.socket;

import com.bright.apollo.enums.AliRegionEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MNServer implements Runnable {

    @Autowired
    private MNService mnService;

    Logger logger = Logger.getLogger(MNServer.class);

    public MNServer() {
        // TODO Auto-generated constructor stub
        logger.info("------ MNServer init ------");
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        logger.info("------ MNServer Thread start ------");
//        MNService mnService = MNService.getInstance(this.enum1);
        mnService.getMNS();

    }
}
