package com.bright.apollo.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.bright.apollo.service.QuartzService;
/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月27日
 * @Version:1.1.0
 */
@Component
public class QuartzRunner implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(QuartzRunner.class);

	@Autowired
	@Lazy
	private QuartzService quartzService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {
		logger.info("===start create table===");
		quartzService.startScheduleByCreateTable();
	}
}
