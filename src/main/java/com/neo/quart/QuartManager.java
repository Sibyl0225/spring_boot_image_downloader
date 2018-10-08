package com.neo.quart;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;

import com.neo.yande.test;

public class QuartManager {
	
	static Logger logger = Logger.getLogger(QuartManager.class);
	
	private static SchedulerFactory sf = new StdSchedulerFactory();
	public static String Yande = "yande";
	public static String Yande_GROUP_NAME = "yande_group";
	public static String Yande_TRIGGER_GROUP_NAME = "yande_trigger_group";
	
	
	
	/**
	 * 
	 * @param jobName
	 * @param groupName
	 * @param triggerGroupName
	 * @param job
	 * @param timeExpression
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName,String groupName,String triggerName,String triggerGroupName,Job job,String timeExpression) throws SchedulerException, ParseException {
		Scheduler scheduler = sf.getScheduler();
		JobDetailImpl jobDetailImpl = new JobDetailImpl();
		jobDetailImpl.setName(jobName);
		jobDetailImpl.setGroup(groupName);
		jobDetailImpl.setJobClass(job.getClass());
		
		Trigger trigger = TriggerBuilder.newTrigger()
				                        .withIdentity(triggerName, triggerGroupName)
				                        .withSchedule(CronScheduleBuilder.cronSchedule(timeExpression))
				                        .build();
		scheduler.scheduleJob(jobDetailImpl, trigger);		
		
		if(scheduler.isInStandbyMode() || scheduler.isShutdown()) {
			scheduler.start();
		}
	}
	
	/**
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param timeExpression
	 * @throws SchedulerException
	 */
	public static void modifyJobTime(String triggerName,String triggerGroupName,String timeExpression) throws SchedulerException {
		Scheduler scheduler = sf.getScheduler();
		CronTrigger trigger = (CronTrigger)scheduler.getTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
		if(trigger == null) {
			logger.warn("job with triggerName:"+triggerName+" triggerGroupName:"+triggerGroupName+" not found!");
			return;
		}
		String cronExpression = trigger.getCronExpression();
		if(cronExpression.equals(timeExpression)) {
			logger.warn("same timeExpression："+timeExpression +"!");
		}else {
			Trigger newTrigger = TriggerBuilder.newTrigger()
                    						   .withIdentity(TriggerKey.triggerKey(triggerName, triggerGroupName))
                    						   .withSchedule(CronScheduleBuilder.cronSchedule(timeExpression))
                    						   .build();
			scheduler.rescheduleJob(TriggerKey.triggerKey(triggerName, triggerGroupName), newTrigger);
		}
		
	}
	
	/**
	 * 
	 * @param jobName
	 * @param groupName
	 * @param triggerName
	 * @param triggerGroupName
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName,String groupName,String triggerName,String triggerGroupName) throws SchedulerException {
		Scheduler scheduler = sf.getScheduler();
		scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroupName));
		scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
	}

}
