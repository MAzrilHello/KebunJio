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
    private String redirectUri;  // This will now point to the ngrok URL

    @Value("${google.application.name}")
    private String applicationName;

    // Generate the authorization URL with the ngrok URL as redirect URI
    public String getAuthorizationUrl() {
        return flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)  // This will use the ngrok URL
                .build();
    }

    // Add event to Google Calendar using the authorization code and event data
    public boolean addEventToCalendar(String code, Event event) {
        try {
            // Exchange the authorization code for an access token
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                    .setRedirectUri(redirectUri)  // Ensure ngrok URL is used here too
                    .execute();

            // Use the access token to create credentials
            GoogleCredential credential = new GoogleCredential()
                    .setAccessToken(tokenResponse.getAccessToken());

            // Build the Google Calendar service with credentials
            Calendar service = new Calendar.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName(applicationName)
                    .build();

            // Convert your event to a Google Calendar event
            com.google.api.services.calendar.model.Event googleEvent = event.toGoogleCalendarEvent();

            // Insert the event into the calendar
            com.google.api.services.calendar.model.Event createdEvent =
                    service.events().insert("primary", googleEvent).execute();

            // Return true if the event was successfully created
            return createdEvent != null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add event to Google Calendar", e);
        }
    }
}
