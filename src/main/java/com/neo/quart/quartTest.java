package com.neo.quart;

import java.text.ParseException;

import org.quartz.SchedulerException;

public class quartTest {

	public static void main(String[] args) {
		String timeExpression = "0/2 * * * * ?";
		try {
			QuartManager.addJob(QuartManager.Yande, QuartManager.Yande_GROUP_NAME, QuartManager.Yande, QuartManager.Yande_TRIGGER_GROUP_NAME, new YandeJob(), timeExpression);
		} catch (SchedulerException | ParseException e) {
			e.printStackTrace();
		}
		
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
			
		try {
			timeExpression =  "0/5 * * * * ?";
			QuartManager.modifyJobTime(QuartManager.Yande, QuartManager.Yande_TRIGGER_GROUP_NAME, timeExpression);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			QuartManager.removeJob(QuartManager.Yande, QuartManager.Yande_GROUP_NAME, QuartManager.Yande, QuartManager.Yande_TRIGGER_GROUP_NAME);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		

	}

}
