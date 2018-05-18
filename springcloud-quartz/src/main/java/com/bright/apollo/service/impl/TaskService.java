package com.bright.apollo.service.impl;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.job.TimeJob;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年4月12日
 * @Version:1.1.0
 */
@Service
public class TaskService {
	@Autowired
	private Scheduler scheduler;
	private static String SERVER_JOB_GROUP_NAME = "OBJobGroup";
	private static String SERVER_TRIGGER_GROUP_NAME = "OBTriggerGroup";

	public void startSchedule(int sceneNumber, String sceneName, String cronString, int group) {
		try {

			// 定义当前调度器的具体作业对象

			JobDetail jobDetail = JobBuilder.newJob(TimeJob.class).withIdentity(
					String.format("%d", sceneNumber) + "_" + String.format("%d", group), SERVER_JOB_GROUP_NAME).build();
			// 定义当前具体作业对象的参数

			JobDataMap jobDataMap = jobDetail.getJobDataMap();

			jobDataMap.put("scene_number", String.format("%d", sceneNumber));

			jobDataMap.put("condition_group", String.format("%d", group));

			// log.info("====cronString:" + cronString);
			//String rightConString = ByteHelper.timeString2Cron(cronString);
			// log.info("====rightConString:" + rightConString);
			// 作业的触发器

			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(String.format("%d", sceneNumber) + "_" + String.format("%d", group),
							SERVER_TRIGGER_GROUP_NAME)
					.withSchedule(CronScheduleBuilder.cronSchedule(cronString)).build();

			// 注册作业和触发器

			scheduler.scheduleJob(jobDetail, cronTrigger);

			// 开始调度任务

			scheduler.start();

		} catch (SchedulerException e) {

			e.printStackTrace();

		}
	}
}
