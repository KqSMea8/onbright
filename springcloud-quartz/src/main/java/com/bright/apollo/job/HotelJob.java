package com.bright.apollo.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.enums.LocationStatusEnum;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.tool.NumberHelper;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年12月20日
 * @Version:1.1.0
 */
public class HotelJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(HotelJob.class);
	@Autowired
	private FeignDeviceClient feignDeviceClient;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("====execute start====");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String location = dataMap.getString("location");
		String mobile = dataMap.getString("mobile");
		logger.info("==location:"+location+"===mobile:"+mobile);
		if(NumberHelper.isNumeric(location)&&NumberHelper.isNumeric(mobile)){
			ResponseObject<TLocation>locationRes= feignDeviceClient.queryLocationByLocationId(Integer.parseInt(location));
			if(locationRes!=null&&locationRes.getData()!=null){
				TLocation tLocation = locationRes.getData();
				tLocation.setStatus(LocationStatusEnum.TREE.getStatus());
				feignDeviceClient.updateLocationByObj(tLocation);
			}
		}
	}

}
