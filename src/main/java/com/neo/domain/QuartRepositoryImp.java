package com.neo.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import com.neo.quart.QuartManager;

@Service
public class QuartRepositoryImp implements QuartRepository {

	@Override
	public List<HashMap<String, Object>> getRunningJobDetails() {			 	
		return QuartManager.getCurrentlyExecutingJobs(QuartManager.JobDetailType);
	}

	@Override
	public List<HashMap<String, Object>> getRunningTriggers() {
		return	QuartManager.getCurrentlyExecutingJobs(QuartManager.TriggerType);
	}

	
	@Override
	public List<HashMap<String, Object>> getJobDetails() {
		return QuartManager.getAllJobs(QuartManager.JobDetailType);
	}

	@Override
	public List<HashMap<String, Object>> getTriggers() {
		return QuartManager.getAllJobs(QuartManager.TriggerType);
	}
}
