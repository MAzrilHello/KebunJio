package iss.nus.edu.sg.sa4106.KebunJio.Models;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class ActivityLog {
	@Id
	private String id;

	private User user;
	private Plant plant;
	private String activityType;
	private String activityDescription;
	private LocalDateTime timestamp;

	public ActivityLog() {}

	public String getLogId() {
		return id;
	}

	public void setLogId(String logId) {
		this.id = logId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Plant getPlant() {
		return plant;
	}

	public void setPlant(Plant plant) {
		this.plant = plant;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}





}
