package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;

public class Session {

	private int sessionId;
	private User user;
	private LocalDateTime startDateTime;
	private LocalDateTime lastActionDateTime;
	private LocalDateTime endDateTime;
	
	public Session() {}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
