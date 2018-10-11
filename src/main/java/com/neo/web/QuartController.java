package com.neo.web;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.neo.domain.QuartRepositoryImp;
import com.neo.quart.QuartManager;
import com.neo.quart.YandeJob;

@RestController
public class QuartController {
	
	private static Logger logger = LogManager.getLogger(QuartController.class.getName());
	
	@Autowired
	private QuartRepositoryImp quartRepository;
	
	@RequestMapping("/startOneJob")
    public void startOneJob() {
		String timeExpression = "0/5 * * * * ?";
		try {
			QuartManager.addJob(QuartManager.Yande, QuartManager.Yande_GROUP_NAME, QuartManager.Yande, QuartManager.Yande_TRIGGER_GROUP_NAME, new YandeJob(),new HashMap<String, Object>(), timeExpression);
		} catch (SchedulerException | ParseException e) {
			e.printStackTrace();
		}
    }
	
	@RequestMapping("/startOtherJob")
    public void startOtherJob() {
		String timeExpression = "0/15 * * * * ?";
		HashMap<String, Object> jobDataMap = new HashMap<String, Object>();
		jobDataMap.put("startPage", 1l);
		jobDataMap.put("endPage", 15l);
		jobDataMap.put("picturQuality", "laegerImage");
		try {
			QuartManager.addJob("yande_image_downloader", QuartManager.Yande_GROUP_NAME, "yande_image_downloader", QuartManager.Yande_TRIGGER_GROUP_NAME, new YandeJob(),jobDataMap, timeExpression);
		} catch (SchedulerException | ParseException e) {
			e.printStackTrace();
		}
    }
	
    @SuppressWarnings("unchecked")
	@RequestMapping("/getJobs")
    public ModelAndView getJobDetails(Locale locale,Model model) {
    	List<HashMap<String, Object>> jobDetails = (List<HashMap<String, Object>>) quartRepository.getJobDetails();
    	logger.info(jobDetails);
        return new ModelAndView("quart/jobDetails", "jobDetails", jobDetails);
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping("/getTriggers")
    public ModelAndView getTriggers(Locale locale,Model model) {
    	List<HashMap<String, Object>> triggers = (List<HashMap<String, Object>>) quartRepository.getTriggers();
    	logger.info(triggers);
        return new ModelAndView("quart/triggers", "triggers", triggers);
    }
}