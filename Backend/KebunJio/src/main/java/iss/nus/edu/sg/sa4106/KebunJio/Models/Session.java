package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;

public class Session {
	@id
	private String id;

	private int userId;
	private LocalDateTime startDateTime;
	private LocalDateTime lastActionDateTime;
	private LocalDateTime endDateTime;
	
	public Session() {}

	public int getSessionId() {
		return id;
	}

	public void setSessionId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getLastActionDateTime() {
		return lastActionDateTime;
	}

	public void setLastActionDateTime(LocalDateTime lastActionDateTime) {
		this.lastActionDateTime = lastActionDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	

}
