package iss.nus.edu.sg.sa4106.KebunJio.Services;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleCalendarService {

    @Autowired
    private GoogleAuthorizationCodeFlow flow;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.application.name}")
    private String applicationName;

    public String getAuthorizationUrl() {
        return flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .build();
    }

    public boolean addEventToCalendar(String code, Event event) {
        try {
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                    .setRedirectUri(redirectUri)
                    .execute();

            GoogleCredential credential = new GoogleCredential()
                    .setAccessToken(tokenResponse.getAccessToken());

            Calendar service = new Calendar.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName(applicationName)
                    .build();

            com.google.api.services.calendar.model.Event googleEvent = event.toGoogleCalendarEvent();
            com.google.api.services.calendar.model.Event createdEvent =
                    service.events().insert("primary", googleEvent).execute();

            return createdEvent != null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add event to Google Calendar", e);
        }
    }
}