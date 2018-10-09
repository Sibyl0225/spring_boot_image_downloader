package com.neo.quart;

import java.text.ParseException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.quartz.SchedulerException;

public class quartTest {

	public static void main(String[] args) throws InterruptedException {
		
		String timeExpression = "0/2 * * * * ?";
		try {
			QuartManager.addJob(QuartManager.Yande, QuartManager.Yande_GROUP_NAME, QuartManager.Yande, QuartManager.Yande_TRIGGER_GROUP_NAME, new YandeJob(),new HashMap<String, Object>(), timeExpression);
		} catch (SchedulerException | ParseException e) {
			e.printStackTrace();
		}
		
		
		TimeUnit.SECONDS.sleep(15);
		
			
		try {
			timeExpression =  "0/5 * * * * ?";
			QuartManager.modifyJobTime(QuartManager.Yande, QuartManager.Yande_TRIGGER_GROUP_NAME, timeExpression);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		TimeUnit.SECONDS.sleep(15);
		
		try {
			QuartManager.removeJob(QuartManager.Yande, QuartManager.Yande_GROUP_NAME, QuartManager.Yande, QuartManager.Yande_TRIGGER_GROUP_NAME);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		

	}

}
