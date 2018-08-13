package com.bright.apollo.job;


import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.service.IntelligentFingerService;
import com.bright.apollo.tool.NumberHelper;



/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月8日  
 *@Version:1.1.0  
 */
public class RemoteUserOpenJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(RemoteUserOpenJob.class);
	@Autowired
	private IntelligentFingerService intelligentFingerService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		try {
			String fingerRemoteUserId = dataMap.getString("fingerRemoteUserId");
			String serialId = dataMap.getString("serialId");
			logger.info("===fingerRemoteUserId:" + fingerRemoteUserId + "===serialId:" + serialId);
			if (NumberHelper.isNumeric(fingerRemoteUserId)) {
				TIntelligentFingerRemoteUser fingerRemoteUser =intelligentFingerService.queryTintelligentFingerRemoteUserById(Integer.parseInt(fingerRemoteUserId));
  				if(fingerRemoteUser!=null){
					fingerRemoteUser.setIsend(1);
					intelligentFingerService.updateTintelligentFingerRemoteUser(fingerRemoteUser);
					TIntelligentFingerAbandonRemoteUser abandonRemoteUser = new TIntelligentFingerAbandonRemoteUser();
					abandonRemoteUser.setSerialid(serialId);
					abandonRemoteUser.setUserSerialid(fingerRemoteUser.getUserSerialid());
					intelligentFingerService.addIntelligentFingerAbandonRemoteUser(abandonRemoteUser);
				}
			}
		} catch (Exception e) {
			logger.error("===error:"+e.getMessage());
 		}
	}


}
