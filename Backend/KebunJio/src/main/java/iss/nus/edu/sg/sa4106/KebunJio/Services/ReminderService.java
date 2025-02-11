package iss.nus.edu.sg.sa4106.KebunJio.Services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Reminder;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.ReminderRepository;

@Service
public class ReminderService {
	
    @Autowired
    private ReminderRepository reminderRepo;

    /**
     * Retrieves reminders for a specific user and plant.
     * @param userId The user ID.
     * @param plantId The plant ID.
     * @return A list of reminders for the specified user and plant.
     */
    public List<Reminder> getRemindersByUserAndPlant(String userId, String plantId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null or empty.");
        }
        if (plantId == null || plantId.isEmpty()) {
            throw new IllegalArgumentException("Plant ID must not be null or empty.");
        }

        return reminderRepo.findByUserIdAndPlantId(userId, plantId);
    }
    
    public List<Reminder> getRemindersByPlant(String plantId) {
        return reminderRepo.findByPlantId(plantId)
                .orElse(Collections.emptyList()); 
    }
    
    public Reminder addReminder(Reminder reminder) {
        // Perform any necessary validation
        if (reminder.getUserId() == null || reminder.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (reminder.getPlantId() == null || reminder.getPlantId().isEmpty()) {
            throw new IllegalArgumentException("Plant ID cannot be null or empty.");
        }
        if (reminder.getReminderType() == null || reminder.getReminderType().isEmpty()) {
            throw new IllegalArgumentException("Reminder type cannot be null or empty.");
        }
        if (reminder.getReminderDateTime() == null) {
            throw new IllegalArgumentException("Reminder date and time cannot be null.");
        }

        // Set default values (if necessary)
        if (reminder.getStatus() == null || reminder.getStatus().isEmpty()) {
            reminder.setStatus("Active");
        }
        if (reminder.getCreatedDateTime() == null) {
            reminder.setCreatedDateTime(LocalDateTime.now());
        }

        // Save the reminder to the database
        return reminderRepo.save(reminder);
    }

    public List<Reminder> getAllReminders() {
        return reminderRepo.findAll();
    }

    public Optional<Reminder> getReminderById(String id) {
        return reminderRepo.findById(id);
    }

    public List<Reminder> getRemindersByUserId(String userId) {
        return reminderRepo.findByUserId(userId);
    }

    public List<Reminder> getRemindersByStatus(String status) {
        return reminderRepo.findByStatus(status);
    }

    public List<Reminder> getRemindersByType(String reminderType) {
        return reminderRepo.findByReminderType(reminderType);
    }

    public List<Reminder> getRemindersWithinTimeframe(LocalDateTime start, LocalDateTime end) {
        return reminderRepo.findByReminderDateTimeBetween(start, end);
    }

    public Optional<Reminder> updateReminder(String id, Reminder updatedReminder) {
        return reminderRepo.findById(id).map(existingReminder -> {
            if (updatedReminder.getReminderType() != null) {
                existingReminder.setReminderType(updatedReminder.getReminderType());
            }
            if (updatedReminder.getReminderDateTime() != null) {
                existingReminder.setReminderDateTime(updatedReminder.getReminderDateTime());
            }
            if (updatedReminder.getIsRecurring() != null) {
                existingReminder.setIsRecurring(updatedReminder.getIsRecurring());
            }
            if (updatedReminder.getRecurrenceInterval() != null) {
                existingReminder.setRecurrenceInterval(updatedReminder.getRecurrenceInterval());
            }
            if (updatedReminder.getStatus() != null) {
                existingReminder.setStatus(updatedReminder.getStatus());
            }
            return reminderRepo.save(existingReminder);
        });
    }

    public boolean deleteReminder(String id) {
        if (reminderRepo.existsById(id)) {
            reminderRepo.deleteById(id);
            return true;
        }
        return false;
    }

}
