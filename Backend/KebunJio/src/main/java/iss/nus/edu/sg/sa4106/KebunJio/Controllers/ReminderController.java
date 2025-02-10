package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Reminder;
import iss.nus.edu.sg.sa4106.KebunJio.Services.ReminderService;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/reminders")
@CrossOrigin(origins = "*")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    /**
     * GET endpoint to retrieve reminders for a specific user and plant.
     * @param userId The user ID.
     * @param plantId The plant ID.
     * @return A list of reminders.
     */
    @GetMapping("/user/{userId}/plant/{plantId}")
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
    
    @PostMapping("/add")
    public ResponseEntity<?> addReminder(@RequestBody Reminder reminder) {
        try {
            Reminder savedReminder = reminderService.addReminder(reminder);
            return ResponseEntity.ok(savedReminder);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An error occurred while adding the reminder.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Reminder>> getAllReminders() {
        return ResponseEntity.ok(reminderService.getAllReminders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reminder> getReminderById(@PathVariable String id) {
        return reminderService.getReminderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reminder>> getRemindersByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(reminderService.getRemindersByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reminder>> getRemindersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(reminderService.getRemindersByStatus(status));
    }

    @GetMapping("/type/{reminderType}")
    public ResponseEntity<List<Reminder>> getRemindersByType(@PathVariable String reminderType) {
        return ResponseEntity.ok(reminderService.getRemindersByType(reminderType));
    }

    @GetMapping("/timeframe")
    public ResponseEntity<List<Reminder>> getRemindersWithinTimeframe(
            @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(reminderService.getRemindersWithinTimeframe(start, end));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable String id, @RequestBody Reminder updatedReminder) {
        return reminderService.updateReminder(id, updatedReminder)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReminder(@PathVariable String id) {
        return reminderService.deleteReminder(id)
                ? ResponseEntity.ok("Reminder deleted successfully.")
                : ResponseEntity.status(404).body("Reminder not found.");
    }
}
