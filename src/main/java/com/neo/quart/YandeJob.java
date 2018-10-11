package com.neo.quart;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.neo.web.QuartController;

public class YandeJob implements Job{

	private static Logger logger = LogManager.getLogger(QuartController.class.getName());
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.info(new SimpleDateFormat().format(new Date()));
		logger.info(jobExecutionContext.getJobDetail().getJobDataMap().toString());
	}

}
