package iss.nus.edu.sg.sa4106.KebunJio.ServiceTest;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.EventRepository;
import iss.nus.edu.sg.sa4106.KebunJio.Services.EventService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.GoogleCalendarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private GoogleCalendarService googleCalendarService;

    @InjectMocks
    private EventService eventService;

    @Test
    public void testCreateEvent() {
        Event event = new Event();
        event.setDescription("Test Event");

        when(eventRepository.save(any(Event.class))).thenReturn(event);
        
        boolean result = eventService.createEvent(event, "mockAuthCode");

        assertTrue(result);
        verify(eventRepository).save(any(Event.class));
    }
}