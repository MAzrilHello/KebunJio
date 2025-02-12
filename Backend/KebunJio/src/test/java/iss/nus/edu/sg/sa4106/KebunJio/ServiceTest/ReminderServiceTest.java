package iss.nus.edu.sg.sa4106.KebunJio.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Reminder;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.ReminderRepository;
import iss.nus.edu.sg.sa4106.KebunJio.Services.ReminderService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // ✅ Use Mockito for testing
public class ReminderServiceTest {

	//e
    @Mock
    private ReminderRepository reminderRepo; // ✅ Mock the database repository

    @InjectMocks
    private ReminderService reminderService; // ✅ Inject mock repository into ReminderService

    @Test
    public void testAddReminder() {
        // Testing reminder service and instantiating data
    	
        Reminder reminder = new Reminder();
        reminder.setUserId("user12");
        reminder.setPlantId("plant45");
        reminder.setReminderType("Watering");
        reminder.setReminderDateTime(LocalDateTime.of(2025, 3, 10, 10, 0));
        reminder.setStatus("Active");

        // Mock the repository save operation
        when(reminderRepo.save(any(Reminder.class))).thenReturn(reminder);

        // Act: Call addReminder()
        Reminder savedReminder = reminderService.addReminder(reminder);

        // Assert: Verify the result
        assertNotNull(savedReminder);
        verify(reminderRepo, times(1)).save(any(Reminder.class));

    }
}
