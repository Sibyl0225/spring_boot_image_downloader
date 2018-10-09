package com.neo.quart;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.neo.util.DateUtil;
import com.neo.yande.test;

public class QuartManager {
	
	static Logger logger = Logger.getLogger(QuartManager.class);
	
	public static String JobDetailType = "0";
	public static String TriggerType = "1";
	
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
	public static void addJob(String jobName,String groupName,String triggerName,String triggerGroupName,Job job,HashMap<String, Object> jobDataMap,String timeExpression) throws SchedulerException, ParseException {
		Scheduler scheduler = sf.getScheduler();
		
		JobDetail jobDetail = JobBuilder.newJob(job.getClass())
										.withIdentity(jobName, groupName)
										.setJobData(new JobDataMap(jobDataMap))
										.build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				                        .withIdentity(triggerName, triggerGroupName)
				                        .withSchedule(CronScheduleBuilder.cronSchedule(timeExpression))
				                        .build();
		scheduler.scheduleJob(jobDetail, trigger);		
		
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
		TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
		CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
		if(trigger == null) {
			logger.warn("job with triggerName:"+triggerName+" triggerGroupName:"+triggerGroupName+" not found!");
			return;
		}
		String cronExpression = trigger.getCronExpression();
		if(cronExpression.equals(timeExpression)) {
			logger.warn("same timeExpression："+timeExpression +"!");
		}else {
			Trigger newTrigger = TriggerBuilder.newTrigger()
                    						   .withIdentity(triggerKey)
                    						   .withSchedule(CronScheduleBuilder.cronSchedule(timeExpression))
                    						   .build();
			scheduler.rescheduleJob(triggerKey, newTrigger);
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
		TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
		scheduler.pauseTrigger(triggerKey);
		scheduler.unscheduleJob(triggerKey);
		scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
	}
	
	public static List<HashMap<String, Object>> getCurrentlyExecutingJobs(String type) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();		
		try {
			Scheduler scheduler = sf.getScheduler();
			List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
			Iterator<JobExecutionContext> jobIterator = currentlyExecutingJobs.iterator();
			while (jobIterator.hasNext()) {
				JobExecutionContext nextJob = jobIterator.next();
				if (type.equals(JobDetailType)) {

					JobDetail jobDetail = nextJob.getJobDetail();
					HashMap<String, Object> jobDetailMap = jobDetailToMap(jobDetail);
					list.add(jobDetailMap);
				} else if (type.equals(TriggerType)) {

					Trigger trigger = nextJob.getTrigger();
					HashMap<String, Object> triggerMap = triggerToMap(trigger);
					list.add(triggerMap);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<HashMap<String, Object>> getAllJobs(String type) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			Scheduler scheduler = sf.getScheduler();
			for (String groupName : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					JobDetail jobDetail = scheduler.getJobDetail(jobKey);
					List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
					if (type.equals(JobDetailType)) {
						HashMap<String, Object> jobDetailMap = jobDetailToMap(jobDetail);
						list.add(jobDetailMap);
					} else if (type.equals(TriggerType)) {
						for (Trigger trigger : triggersOfJob) {
							HashMap<String, Object> triggerMap = triggerToMap(trigger);
							list.add(triggerMap);
						}
					}
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static HashMap<String,Object> jobDetailToMap(JobDetail jobDetail) {
		HashMap<String, Object> jobDetailMap = new HashMap<String,Object>();
		JobKey jobKey = jobDetail.getKey();
		jobDetailMap.put("JobName", jobKey.getName());
		jobDetailMap.put("JobGroupName", jobKey.getGroup());
		Iterator<Entry<String, Object>> iterator = jobDetail.getJobDataMap().entrySet().iterator();
		HashMap<String, Object> jobDataMap = new HashMap<String,Object>();
		while(iterator.hasNext()) {
			Entry<String, Object> next = iterator.next();
			jobDataMap.put(next.getKey(), next.getValue());
		}
		jobDetailMap.put("JobDataMap", jobDataMap.toString());
		return jobDetailMap;
		
	}
	/**
	 *  getKey 获取触发器key值   
		getJobKey  获取作业key 
		getDescription 获取面熟  
		getCalendarName 获取日历名称  
		getJobDataMap 获取作业数据map  
		getPriority 获取优先级  
		mayFireAgain 是否重复执行  
		getStartTime 开始时间  
		getEndTime 结束时间  
		getNextFireTime 下一次执行时间  
		getPreviousFireTime 上一执行时间  
		getFireTimeAfter(Date afterTime) 获取某个时间后的运行时间     
		getFinalFireTime 获取最后执行时间  
		getMisfireInstruction 获取失败策略  
		getTriggerBuilder 获取触发器建造者    
		getScheduleBuilder 获取调度类建造者
	 * @param trigger
	 * @return
	 */
	
	private static HashMap<String,Object> triggerToMap(Trigger trigger) {
		HashMap<String, Object> triggerMap = new HashMap<String,Object>();
		CronTrigger cronTrigger = (CronTrigger)trigger;
		TriggerKey triggerKey = cronTrigger.getKey();
		triggerMap.put("TriggerName", triggerKey.getName());
		triggerMap.put("TriggerGroupName", triggerKey.getGroup());
		triggerMap.put("MayFireAgain", cronTrigger.mayFireAgain());
		triggerMap.put("Priority", cronTrigger.getPriority());
		triggerMap.put("StartTime", DateUtil.format(cronTrigger.getStartTime()));
		triggerMap.put("PreviousFireTime", DateUtil.format(cronTrigger.getPreviousFireTime()));
		triggerMap.put("NextFireTime", DateUtil.format(cronTrigger.getNextFireTime()));
		triggerMap.put("CronExpression", cronTrigger.getCronExpression());
		return triggerMap;		
	}

}
