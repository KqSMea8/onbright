package com.bright.apollo.job;

import com.bright.apollo.common.entity.TAliDevTimer;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.AliDeviceService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class TimerJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(TimerJob.class);

    @Autowired
    private FeignAliClient feignAliClient;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("=============TimerJob execute================");
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String timerId = dataMap.getString("timerId");
        log.info("==timerId:"+timerId);
        if (!StringUtils.isEmpty(timerId)) {
            try {
                Integer id = Integer.valueOf(timerId);
                ResponseObject res = feignAliClient.getAliDevTimerById(id);
                if (res.getData() != null) {
                    feignAliClient.addTimerAction(id);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}
