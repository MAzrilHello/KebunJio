package iss.nus.edu.sg.sa4106.KebunJio.Services;

import java.time.LocalDateTime;
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

    public Reminder addReminder(Reminder reminder) {
        // Ensure required fields are set with defaults if needed
        reminder.setCreatedDateTime(LocalDateTime.now());
        if (reminder.getStatus() == null || reminder.getStatus().isEmpty()) {
            reminder.setStatus("Active");
        }
        if (reminder.getIsRecurring() == null) {
            reminder.setIsRecurring(false);
        }
        if (reminder.getRecurrenceInterval() == null || reminder.getRecurrenceInterval().isEmpty()) {
            reminder.setRecurrenceInterval("None");
        }
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
