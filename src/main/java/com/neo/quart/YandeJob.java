package com.neo.quart;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.neo.yande.test;

public class YandeJob implements Job{

	static Logger logger = Logger.getLogger(YandeJob.class);
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.info(new SimpleDateFormat().format(new Date()));
		logger.info(jobExecutionContext.getJobDetail().getJobDataMap().toString());
	}

}
