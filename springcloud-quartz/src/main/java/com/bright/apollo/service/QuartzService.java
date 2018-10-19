package com.bright.apollo.service;

import com.bright.apollo.job.TimerJob;
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

import com.bright.apollo.job.CreateTableJob;
import com.bright.apollo.job.RemoteUserOpenJob;
import com.bright.apollo.job.TimeJob;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.DateHelper;
import com.bright.apollo.tool.MD5;

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
	private static String SERVER_REMOTE_OPEN_TASK = "RemoteOpen";
	private static String SERVER_TIMER_JOB_NAME = "OBTimerJob";
	private static String SERVER_TRIGGER_TIMER_JOB_NAME = "OBTimerJobTrigger";
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
			// String rightConString = cronString;
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
			scheduler.pauseTrigger(new TriggerKey(jobName, SERVER_TRIGGER_GROUP_NAME));
			scheduler.unscheduleJob(new TriggerKey(jobName, SERVER_TRIGGER_GROUP_NAME));
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
			scheduler.pauseTrigger(new TriggerKey(jobName, SERVER_TRIGGER_GROUP_NAME));
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
			scheduler.resumeTrigger(new TriggerKey(jobName, SERVER_TRIGGER_GROUP_NAME));
		} catch (Exception e) {
			log.error("===error msg:" + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param fingerRemoteUserId
	 * @param endTime
	 * @param serialId
	 * @throws Exception
	 * @Description:
	 */
	public void startRemoteOpenTaskSchedule(int fingerRemoteUserId, String endTime, String serialId) {
		// 得到默认的调度器
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(RemoteUserOpenJob.class)
					.withIdentity(MD5.getMD5Str(fingerRemoteUserId + serialId), SERVER_REMOTE_OPEN_TASK).build();
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			jobDataMap.put("fingerRemoteUserId", String.format("%d", fingerRemoteUserId));
			jobDataMap.put("serialId", String.format("%s", serialId));
			String rightConString = Date2Cron(endTime);

			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(String.format("%d", fingerRemoteUserId) + "_" + String.format("%s", serialId),
							SERVER_TRIGGER_GROUP_NAME)
					.withSchedule(CronScheduleBuilder.cronSchedule(rightConString)).build();
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
		} catch (Exception e) {
			log.info("===the quartz job exist===");
			log.error("message:" + e.getMessage());
		}
	}

	public void startTimerSchedule(int timerId,String cronString){
		try {

			// 得到默认的调度器

			Scheduler scheduler = schedulerFactory.getScheduler();

			// 定义当前调度器的具体作业对象

			JobDetail jobDetail = JobBuilder
					.newJob(TimerJob.class)
					.withIdentity(
							String.format("%d", timerId),
							SERVER_TIMER_JOB_NAME).build();

			// 定义当前具体作业对象的参数

			JobDataMap jobDataMap = jobDetail.getJobDataMap();

			jobDataMap.put("timerId", String.format("%d", timerId));

			log.info("====cronString:"+cronString);
			// 作业的触发器

			CronTrigger cronTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity(
							String.format("%d", timerId),
							SERVER_TRIGGER_TIMER_JOB_NAME)
					.withSchedule(
							CronScheduleBuilder.cronSchedule(cronString))
					.build();

			// 注册作业和触发器

			scheduler.scheduleJob(jobDetail, cronTrigger);

			// 开始调度任务

			scheduler.start();

		} catch (SchedulerException e) {

			e.printStackTrace();

		}
	}

	/**
	 * 
	 * @Description:
	 */
	public void startScheduleByCreateTable() {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(CreateTableJob.class)
					.withIdentity("CreateTableJob", Scheduler.DEFAULT_GROUP).build();
			String cronExpression = "0 55 23 L * ?"; // 每个月的最后一天
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity("CreateTableJob", Scheduler.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
			if (!scheduler.checkExists(jobDetail.getKey())) {
				scheduler.scheduleJob(jobDetail, cronTrigger);
				scheduler.start();
			}
		} catch (SchedulerException e) {
			log.info("===the quartz job exist===");
			log.error("message:" + e.getMessage());
		}
	}

	/**
	 * @param endTime
	 * @return
	 * @Description:
	 */
	private String Date2Cron(String endTime) {
		// 1528283520000
		// 2018,06,06,19,12
		String formatDate = DateHelper.formatDate(Long.parseLong(endTime), DateHelper.FORMATNALLWITHPOINT);
		String[] split = formatDate.split(",");
		if (split == null || split.length <= 0)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append("0 ");
		// yyyy.MM.dd.HH.mm
		// 0 15 10 15 5 2018
		for (int i = split.length - 1; i >= 0; i--) {
			if (i != 0) {
				if (i == 1)
					sb.append(split[i]).append(" ").append("? ");
				else
					sb.append(split[i]).append(" ");
			} else
				sb.append(split[i]);
		}
		return sb.toString();
	}

}
