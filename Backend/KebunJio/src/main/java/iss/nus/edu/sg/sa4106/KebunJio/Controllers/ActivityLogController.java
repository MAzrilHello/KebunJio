package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.OtherReusables.Reusables;
import iss.nus.edu.sg.sa4106.KebunJio.Services.ActivityLogService;
import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/ActivityLog")
@CrossOrigin(origins = "*")
public class ActivityLogController {
	@Autowired
	private ActivityLogService actLogService;
	
	@PostMapping
	public ResponseEntity<ActivityLog> makeNewActLog(@RequestBody ActivityLog actLog, HttpSession sessionObj) {
		// first validate that we have permission
		System.out.println("ActivityLog: getting currentUser");
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		System.out.println("ActivityLog: checking permissions");
		HttpStatus editStatus = Reusables.editStatusType(currentUser, actLog.getUserId());
		if (editStatus != HttpStatus.OK) {
			return new ResponseEntity<ActivityLog>(editStatus);
		}
		System.out.println("ActivityLog: create new activity log");
		try {
        	ActivityLog newActLog = new ActivityLog();
        	newActLog.setUserId(actLog.getUserId());
        	newActLog.setPlantId(actLog.getPlantId());
        	newActLog.setActivityType(actLog.getActivityType());
        	newActLog.setActivityDescription(actLog.getActivityDescription());
        	newActLog.setTimestamp(actLog.getTimestamp());
        	return ResponseEntity.ok(actLogService.save(newActLog));
		} catch(Exception e) {
			System.out.println(e);
			return ResponseEntity.internalServerError().build();
		}
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
	public ResponseEntity<ActivityLog> updateActivityLog(@PathVariable String logId, @RequestBody ActivityLog actLog, HttpSession sessionObj) {
		System.out.println("ActivityLog: getting currentUser");
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		try {
            Optional<ActivityLog> findActLog = actLogService.getActivityLog(logId);
            if (findActLog.isPresent()) {
            	ActivityLog foundActLog = findActLog.get();

            	System.out.println("ActivityLog: checking permissions");
            	HttpStatus editStatus = Reusables.editStatusType(currentUser, foundActLog.getUserId());
        		if (editStatus != HttpStatus.OK) {
        			return new ResponseEntity<ActivityLog>(editStatus);
        		}
            	
            	foundActLog.setUserId(actLog.getUserId());
            	foundActLog.setPlantId(actLog.getPlantId());
            	foundActLog.setActivityType(actLog.getActivityType());
            	foundActLog.setActivityDescription(actLog.getActivityDescription());
            	foundActLog.setTimestamp(actLog.getTimestamp());
            	return ResponseEntity.ok(actLogService.save(foundActLog));
            } else {
            	return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
			System.out.println(e);
			return ResponseEntity.internalServerError().build();
        }
	}
	
	@DeleteMapping("/{logId}")
	public ResponseEntity<?> deleteActivityLog(@PathVariable String logId, HttpSession sessionObj) {
		// first validate that we have permission
		System.out.println("ActivityLog: getting currentUser");
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		// check if it even exists
		Optional<ActivityLog> findActLog = actLogService.getActivityLog(logId);
		if (!findActLog.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		System.out.println("ActivityLog: checking permissions");
		ActivityLog actLog = findActLog.get();
		HttpStatus editStatus = Reusables.editStatusType(currentUser, actLog.getUserId());
		if (editStatus != HttpStatus.OK) {
			return new ResponseEntity<ActivityLog>(editStatus);
		}
		
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
