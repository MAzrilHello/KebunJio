package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import iss.nus.edu.sg.sa4106.KebunJio.Services.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/google/calendar")
@CrossOrigin(origins = "*")
public class GoogleCalendarController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @GetMapping("/auth-url")
    public ResponseEntity<?> getAuthUrl() {
        try {
            String authUrl = googleCalendarService.getAuthorizationUrl();
            return ResponseEntity.ok(Map.of("authUrl", authUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to generate authorization URL: " + e.getMessage()));
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<?> handleCallback(@RequestBody Map<String, Object> request) {
        try {
            String code = (String) request.get("code");
            Event eventData = convertMapToEvent((Map<String, Object>) request.get("eventData"));

            boolean success = googleCalendarService.addEventToCalendar(code, eventData);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to add event to calendar: " + e.getMessage()));
        }
    }

    private Event convertMapToEvent(Map<String, Object> eventMap) {
        Event event = new Event();
//        event.setEventId(((Number) eventMap.get("eventId")).intValue());
        event.setName((String) eventMap.get("title"));
        event.setDescription((String) eventMap.get("description"));
        event.setLocation((String) eventMap.get("location"));
        // Parse dates appropriately
        event.setStartDateTime(java.time.LocalDateTime.parse((String) eventMap.get("startTime")));
        event.setEndDateTime(java.time.LocalDateTime.parse((String) eventMap.get("endTime")));
        event.setPicture((String) eventMap.get("picture"));
        return event;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}