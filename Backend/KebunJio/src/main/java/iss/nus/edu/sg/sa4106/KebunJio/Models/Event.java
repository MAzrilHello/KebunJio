package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;

public class Event {
	
	private int eventId;
	private String name;
	private String location;
	private LocalDateTime StartDateTime;
	private LocalDateTime EndDateTime;
	private String description;
	private String Picture;

	public Event() {}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
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
		return StartDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		StartDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return EndDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		EndDateTime = endDateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return Picture;
	}

	public void setPicture(String picture) {
		Picture = picture;
	}
	
	

}
