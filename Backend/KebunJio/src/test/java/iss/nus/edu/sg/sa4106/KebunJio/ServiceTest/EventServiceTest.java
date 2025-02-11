package iss.nus.edu.sg.sa4106.KebunJio.ServiceTest;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.EventRepository;
import iss.nus.edu.sg.sa4106.KebunJio.Services.EventService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.GoogleCalendarService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private GoogleCalendarService googleCalendarService;

    @InjectMocks
    private EventService eventService;

    @Test
    public void testCreateEvent() {
        // Arrange: Create test data
        Event event = new Event();
        event.setDescription("Test Event");
        String authorizationCode = "mockAuthCode";

        // Mock external service behavior
        when(googleCalendarService.addEventToCalendar(eq(authorizationCode), any(Event.class)))
                .thenReturn(true);

        // Act: Execute test
        boolean result = eventService.createEvent(event, authorizationCode);

        // Assert: Verify results
        assertTrue(result);
        verify(googleCalendarService).addEventToCalendar(eq(authorizationCode), any(Event.class));
    }

}