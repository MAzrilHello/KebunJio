package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iss.nus.edu.sg.sa4106.KebunJio.Models.ActivityLog;
import iss.nus.edu.sg.sa4106.KebunJio.Services.ActivityLogService;



@RestController
@RequestMapping("/ActivityLog")
@CrossOrigin(origins = "*")
public class ActivityLogController {
	@Autowired
	private ActivityLogService actLogService;
	
	@PostMapping
	public ActivityLog makeNewActLog(@RequestBody ActivityLog actLog) {
		return actLogService.save(actLog);
	}
	
	@GetMapping("/{logId}")
	public ResponseEntity<ActivityLog> getActivityLog(@PathVariable String logId) {
        try {
            Optional<ActivityLog> findActLog = actLogService.getActivityLog(logId);
            if (findActLog.isPresent()) {
            	return ResponseEntity.ok(findActLog.get());
            } else {
            	return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
	}
	
	@GetMapping("/Users/{userId}")
	public ResponseEntity<List<ActivityLog>> getUserActivities(@PathVariable String userId) {
        try {
            List<ActivityLog> findActLog = actLogService.getAllUserActivityLog(userId);
            return ResponseEntity.ok(findActLog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
	}
	
	@GetMapping("/Plants/{plantId}")
	public ResponseEntity<List<ActivityLog>> getPlantActivities(@PathVariable String plantId) {
        try {
            List<ActivityLog> findActLog = actLogService.getAllPlantActivityLog(plantId);
            if (findActLog.size() > 0) {
            	return ResponseEntity.ok(findActLog);
            } else {
            	return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
	}
	
	@PutMapping("/{logId}")
	public ResponseEntity<ActivityLog> updateActivityLog(@PathVariable String logId, @RequestBody ActivityLog actLog) {
        try {
            Optional<ActivityLog> findActLog = actLogService.getActivityLog(logId);
            if (findActLog.isPresent()) {
            	ActivityLog foundActLog = findActLog.get();
            	foundActLog.setUserId(actLog.getUserId());
            	foundActLog.setPlantId(actLog.getPlantId());
            	foundActLog.setActivityType(actLog.getActivityType());
            	foundActLog.setActivityDescription(actLog.getActivityDescription());
            	foundActLog.setTimestamp(actLog.getTimestamp());
            	return ResponseEntity.ok(findActLog.get());
            } else {
            	return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
	}
	
	@DeleteMapping("/{logId}")
	public ResponseEntity<?> deleteActivityLog(@PathVariable String logId) {
		try {
			if (actLogService.deleteActivityLog(logId)) {
				return ResponseEntity.ok().build();
            } else {
            	return ResponseEntity.notFound().build();
            }
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
