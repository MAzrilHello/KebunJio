package iss.nus.edu.sg.sa4106.KebunJio.Services;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.EventRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
    // Change the Id to String
    public Event findByEventId(String eventId) {
        return eventRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void deleteByEventId(String eventId) {
        eventRepository.deleteByEventId(eventId);
    }

    public boolean createEvent(Event event, String authorizationCode) {
        try {
            return googleCalendarService.addEventToCalendar(authorizationCode, event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create event", e);
        }
    }

    public Event getEvent(Long id) {
        return null;
    }

    public void updateEvent(Event event) {
    }

    public void deleteEvent(Long id) {
    }
}
