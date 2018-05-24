package com.bright.apollo.socket;

import com.bright.apollo.enums.AliRegionEnum;
import org.apache.log4j.Logger;

public class MNServer implements Runnable {
    private AliRegionEnum enum1;

    Logger logger = Logger.getLogger(MNServer.class);
    public MNServer(AliRegionEnum enum1) {
        // TODO Auto-generated constructor stub
        logger.info("------ MNServer init ------");
        this.enum1 = enum1;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        logger.info("------ MNServer Thread start ------");
        MNService mnService = MNService.getInstance(this.enum1);
        mnService.getMNS(enum1);

    }
}
