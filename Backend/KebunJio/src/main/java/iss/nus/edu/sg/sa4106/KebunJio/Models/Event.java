package iss.nus.edu.sg.sa4106.KebunJio.Models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Date;

@Document(collection = "events")
public class Event {

	@Id
	private String id;
//	private int eventId;

	private String name;
	private String location;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String description;
	private String picture;

	public Event() {}


	public Event(String id, String name, String location,
				 LocalDateTime startDateTime, LocalDateTime endDateTime,
				 String description, String picture) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.description = description;
		this.picture = picture;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public com.google.api.services.calendar.model.Event toGoogleCalendarEvent() {
		com.google.api.services.calendar.model.Event googleEvent = new com.google.api.services.calendar.model.Event()
				.setSummary(this.name)
				.setDescription(this.description)
				.setLocation(this.location);

		Date startDate = Date.from(this.startDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(this.endDateTime.atZone(ZoneId.systemDefault()).toInstant());

		com.google.api.services.calendar.model.EventDateTime start = new com.google.api.services.calendar.model.EventDateTime()
				.setDateTime(new com.google.api.client.util.DateTime(startDate));

		com.google.api.services.calendar.model.EventDateTime end = new com.google.api.services.calendar.model.EventDateTime()
				.setDateTime(new com.google.api.client.util.DateTime(endDate));

		googleEvent.setStart(start);
		googleEvent.setEnd(end);

		return googleEvent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Event event = (Event) o;
		return Objects.equals(id, event.id) &&
				Objects.equals(name, event.name) &&
				Objects.equals(location, event.location) &&
				Objects.equals(startDateTime, event.startDateTime) &&
				Objects.equals(endDateTime, event.endDateTime) &&
				Objects.equals(description, event.description) &&
				Objects.equals(picture, event.picture);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, location, startDateTime, endDateTime, description, picture);
	}

	@Override
	public String toString() {
		return "Event{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", location='" + location + '\'' +
				", startDateTime=" + startDateTime +
				", endDateTime=" + endDateTime +
				", description='" + description + '\'' +
				", picture='" + picture + '\'' +
				'}';
	}
}