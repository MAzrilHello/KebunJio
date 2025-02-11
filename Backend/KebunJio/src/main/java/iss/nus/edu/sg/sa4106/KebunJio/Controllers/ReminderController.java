package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Reminder;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.OtherReusables.Reusables;
import iss.nus.edu.sg.sa4106.KebunJio.Services.ReminderService;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Reminders")
@CrossOrigin(origins = "*")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @GetMapping("/Plant/{plantId}")
    public ResponseEntity<?> getRemindersByPlant(@PathVariable String plantId) {
        try {
            System.out.println("Fetching reminders for plantId: " + plantId);

            // Call service method
            List<Reminder> reminders = reminderService.getRemindersByPlant(plantId);

            // If no reminders found, return 404
            if (reminders.isEmpty()) {
                System.out.println("No reminders found for plantId: " + plantId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reminders found for this plant.");
            }

            System.out.println("Fetched reminders: " + reminders);
            return ResponseEntity.ok(reminders);
        } catch (Exception ex) {
            System.err.println("Unexpected error while fetching reminders for plantId: " + plantId);
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred while fetching reminders.");
        }
    }    

    @GetMapping("/user/{userId}/Plant/{plantId}")
    public ResponseEntity<?> getRemindersByUserAndPlant(@PathVariable String userId, @PathVariable String plantId) {
        try {
            // Call service method
            List<Reminder> reminders = reminderService.getRemindersByUserAndPlant(userId, plantId);
            return ResponseEntity.ok(reminders);
        } catch (IllegalArgumentException ex) {
            // Handle invalid inputs
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // Handle unexpected errors
            return ResponseEntity.internalServerError().body("An error occurred while fetching reminders.");
        }
    }
    
    // Non-owners can still view.
    @GetMapping
    public ResponseEntity<List<Reminder>> getAllReminders() {
        return ResponseEntity.ok(reminderService.getAllReminders());
    }

    // Non-owners can still view.
    @GetMapping("/{id}")
    public ResponseEntity<Reminder> getReminderById(@PathVariable String id) {
        return reminderService.getReminderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Non-owners can still view.
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reminder>> getRemindersByUserId(@PathVariable String userId) {
        System.out.println("Fetching reminders for userId: " + userId);
        try {
            List<Reminder> reminders = reminderService.getRemindersByUserId(userId);
            System.out.println("Fetched reminders: " + reminders);
            return ResponseEntity.ok(reminders);
        } catch (Exception e) {
            System.err.println("Error fetching reminders for userId: " + userId);
            e.printStackTrace(); 
            return ResponseEntity.status(500).body(null);
        }
    }

    // Non-owners can still view.
    @GetMapping("/Status/{status}")
    public ResponseEntity<List<Reminder>> getRemindersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(reminderService.getRemindersByStatus(status));
    }

    // Non-owners can still view.
    @GetMapping("/Type/{reminderType}")
    public ResponseEntity<List<Reminder>> getRemindersByType(@PathVariable String reminderType) {
        return ResponseEntity.ok(reminderService.getRemindersByType(reminderType));
    }

    // Non-owners can still view.
    @GetMapping("/timeframe")
    public ResponseEntity<List<Reminder>> getRemindersWithinTimeframe(
            @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(reminderService.getRemindersWithinTimeframe(start, end));
    }
    
      // Must restrict to owner only
    @PostMapping("/Add")
    public ResponseEntity<Reminder> addReminder(@RequestBody Reminder reminder, HttpSession sessionObj) {
    	// first validate that we have permission
    	User currentUser = (User) sessionObj.getAttribute("loggedInUser");
    	// Must restrict to owner only
    	HttpStatus editStatus = Reusables.editStatusType(currentUser, reminder.getUserId());
    	if (editStatus != HttpStatus.OK) {
    		return new ResponseEntity<Reminder>(editStatus);
    	}
    	try {
    		Reminder newReminder = new Reminder();
    		newReminder.setUserId(reminder.getUserId());
    		newReminder.setPlantId(reminder.getPlantId());
    		newReminder.setReminderType(reminder.getReminderType());
    		newReminder.setReminderDateTime(reminder.getReminderDateTime());
    		newReminder.setIsRecurring(reminder.getIsRecurring());
    		newReminder.setRecurrenceInterval(reminder.getRecurrenceInterval());
    		newReminder.setStatus(reminder.getStatus());
    		newReminder.setCreatedDateTime(LocalDateTime.now());
    		return ResponseEntity.ok(reminderService.addReminder(newReminder));
    	} catch (Exception e) {
    		System.out.println(e);
			return ResponseEntity.internalServerError().build();
    	}
    }


    // Must restrict to owner only
    @PutMapping("/{id}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable String reminderId,
    												@RequestBody Reminder updatedReminder,
    												HttpSession sessionObj) {
    	Optional<Reminder> existingReminder = reminderService.getReminderById(reminderId);
    	if (existingReminder.isPresent()) {
    		Reminder foundReminder = existingReminder.get();
    		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
    		HttpStatus editStatus = Reusables.editStatusType(currentUser, foundReminder.getUserId());
    		if (editStatus != HttpStatus.OK) {
    			return new ResponseEntity<Reminder>(editStatus);
    		}
    		return reminderService.updateReminder(reminderId, updatedReminder)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    	} else {
    		return ResponseEntity.notFound().build();
    	}
        
    }

    // Must restrict to owner only
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReminder(@PathVariable String reminderId, HttpSession sessionObj) {
    	// first validate that we have permission
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		// check if it even exists
		Optional<Reminder> existingReminder = reminderService.getReminderById(reminderId);
		if (!existingReminder.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Reminder reminder = existingReminder.get();
		HttpStatus editStatus = Reusables.editStatusType(currentUser, reminder.getUserId());
		if (editStatus != HttpStatus.OK) {
			return new ResponseEntity<>(editStatus);
		}
        return reminderService.deleteReminder(reminderId)
                ? ResponseEntity.ok("Reminder deleted successfully.")
                : ResponseEntity.status(404).body("Reminder not found.");
    }
}
