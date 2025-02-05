package iss.nus.edu.sg.sa4106.KebunJio.Services;

import com.mongodb.DuplicateKeyException;
import iss.nus.edu.sg.sa4106.KebunJio.Models.ActivityLog;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository actLogRepo;

    // make a new activity log
    @Transactional
    public ActivityLog save(ActivityLog newActLog) {
    	return actLogRepo.save(newActLog);
    }
    
    // get a activity log
    public Optional<ActivityLog> getActivityLog(String logId) {
    	return actLogRepo.findById(logId);	
    }
    
    // get all activity logs by a specific user
    public List<ActivityLog> getAllUserActivityLog(String userId) {
    	return actLogRepo.findByUserId(userId);
    }
    
 // get all activity logs by a specific plant
    public List<ActivityLog> getAllPlantActivityLog(String plantId) {
    	return actLogRepo.findByPlantId(plantId);
    }
    
    // update an activity log
    public boolean updateActivityLog(ActivityLog updatedActLog) {
    	// check if it exists
    	if (getActivityLog(updatedActLog.getId()).isPresent() == false) {
    		return false;
    	}
    	actLogRepo.save(updatedActLog);
    	return true;
    }
    
    // delete an activity log
    public boolean deleteActivityLog(String actLogId) {
    	// check if it exists
    	Optional<ActivityLog> findActLog = getActivityLog(actLogId);
    	if (findActLog.isPresent() == false) {
    		return false;
    	}
    	ActivityLog theActLog = findActLog.get();
    	actLogRepo.delete(theActLog);
    	return true;
    }

}
