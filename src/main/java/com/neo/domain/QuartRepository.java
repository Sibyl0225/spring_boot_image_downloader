package com.neo.domain;

import java.util.HashMap;
import java.util.List;

public interface QuartRepository  {
	
	List<HashMap<String, Object>> getRunningJobDetails();

	List<HashMap<String, Object>> getRunningTriggers();

	List<HashMap<String, Object>> getJobDetails();

	List<HashMap<String, Object>> getTriggers();
    
}