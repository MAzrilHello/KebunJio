package iss.nus.edu.sg.sa4106.KebunJio.Services;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.EventRepository;
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

    public Event findById(String id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(String id) {
        eventRepository.deleteById(id);
    }

    public boolean createEvent(Event event, String authorizationCode) {
        try {
            return googleCalendarService.addEventToCalendar(authorizationCode, event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create event", e);
        }
    }
}