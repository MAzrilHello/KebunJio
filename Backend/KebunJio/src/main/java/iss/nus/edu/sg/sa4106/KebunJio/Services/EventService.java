package iss.nus.edu.sg.sa4106.KebunJio.Services;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();

    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);

    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Optional<Event> updateEvent(String id, Event event) {
        if (!eventRepository.existsById(id)) {
            return Optional.empty();
        }
        Event newevent = eventRepository.findById(id).get();

        newevent.setDescription(event.getDescription());
        return Optional.of(eventRepository.save(newevent));
    }

    public boolean deleteEvent(String id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean createEventWithGoogleCalendar(Event event, String authorizationCode) {
        try {
            return googleCalendarService.addEventToCalendar(authorizationCode, event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create event", e);
        }
    }

}
