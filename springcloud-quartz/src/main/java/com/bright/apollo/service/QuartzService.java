package com.bright.apollo.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.bright.apollo.job.TimeJob;
import com.bright.apollo.tool.ByteHelper;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月16日
 * @Version:1.1.0
 */
@Service
public class QuartzService {
	private static final Logger log = LoggerFactory.getLogger(QuartzService.class);
	private static String SERVER_JOB_GROUP_NAME = "OBJobGroup";
	private static String SERVER_TRIGGER_GROUP_NAME = "OBTriggerGroup";
	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	/**
	 * 开始一个cronTrigger()调度
	 */
	public void startSchedule(int sceneNumber, String sceneName, String cronString, int group) {
		try {
			// 得到默认的调度器
			Scheduler scheduler = schedulerFactory.getScheduler();
			// 定义当前调度器的具体作业对象
			JobDetail jobDetail = JobBuilder.newJob(TimeJob.class).withIdentity(
					String.format("%d", sceneNumber) + "_" + String.format("%d", group), SERVER_JOB_GROUP_NAME).build();
			// 定义当前具体作业对象的参数
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			jobDataMap.put("scene_number", String.format("%d", sceneNumber));
			jobDataMap.put("condition_group", String.format("%d", group));
			log.info("====cronString:" + cronString);
			String rightConString = ByteHelper.timeString2Cron(cronString);
			log.info("====rightConString:" + rightConString);
			//String rightConString = cronString;
			// 作业的触发器
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(String.format("%d", sceneNumber) + "_" + String.format("%d", group),
							SERVER_TRIGGER_GROUP_NAME)
					.withSchedule(CronScheduleBuilder.cronSchedule(rightConString)).build();
			// 注册作业和触发器
			scheduler.scheduleJob(jobDetail, cronTrigger);
			// 开始调度任务
			scheduler.start();
		} catch (SchedulerException e) {
			log.error("===error msg:" + e.getMessage());
			e.printStackTrace();

		}
	}
	/**  
	 * @param jobName  
	 * @Description:  
	 */
	public void deleteJob(String jobName) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.pauseTrigger(new TriggerKey(jobName,
					SERVER_TRIGGER_GROUP_NAME));
			scheduler.unscheduleJob(new TriggerKey(jobName,
					SERVER_TRIGGER_GROUP_NAME));
			scheduler.deleteJob(new JobKey(jobName, SERVER_JOB_GROUP_NAME));
		} catch (Exception e) {
			log.error("===error msg:" + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	/**  
	 * @param jobName  
	 * @Description:  
	 */
	public void pauseJob(String jobName) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.pauseTrigger(new TriggerKey(jobName,
					SERVER_TRIGGER_GROUP_NAME));
		} catch (Exception e) {
			log.error("===error msg:" + e.getMessage());
 			throw new RuntimeException(e);
		}
	}
	/**  
	 * @param jobName  
	 * @Description:  
	 */
	public void resumeJob(String jobName) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.resumeTrigger(new TriggerKey(jobName,
					SERVER_TRIGGER_GROUP_NAME));
		} catch (Exception e) {
			log.error("===error msg:" + e.getMessage());
 			throw new RuntimeException(e);
		}
	}
}