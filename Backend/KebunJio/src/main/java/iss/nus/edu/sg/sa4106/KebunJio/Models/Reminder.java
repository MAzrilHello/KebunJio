package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Reminders")
public class Reminder {
	@Id
	private String id;  
    private String userId;
    private String plantId; 
    private String reminderType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reminderDateTime;
    private Boolean isRecurring;
    private String recurrenceInterval;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDateTime; 

    
    public Reminder() {}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public String getReminderType() {
		return reminderType;
	}

	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}

	public LocalDateTime getReminderDateTime() {
		return reminderDateTime;
	}

	public void setReminderDateTime(LocalDateTime reminderDateTime) {
		this.reminderDateTime = reminderDateTime;
	}

	public Boolean getIsRecurring() {
		return isRecurring;
	}

	public void setIsRecurring(Boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public String getRecurrenceInterval() {
		return recurrenceInterval;
	}

	public void setRecurrenceInterval(String recurrenceInterval) {
		this.recurrenceInterval = recurrenceInterval;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	} 

}
