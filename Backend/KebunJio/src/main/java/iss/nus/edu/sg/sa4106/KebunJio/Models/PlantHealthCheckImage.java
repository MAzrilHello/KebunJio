package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "plantHealthCheckImages") 
public class PlantHealthCheckImage {
	
	@Id
	private String id;
	private String userId;
    private String plantId;
	private String imageUrl;
	private String imageSource;
    private String plantHealth;
	private LocalDateTime createdDateTime;
	
	public PlantHealthCheckImage() {}

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageSource() {
		return imageSource;
	}

	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}

	public String getPlantHealth() {
		return plantHealth;
	}

	public void setPlantHealth(String plantHealth) {
		this.plantHealth = plantHealth;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	@Override
	public String toString() {
		return "PlantHealthCheckImage [id=" + id + ", userId=" + userId + ", plantId=" + plantId + ", imageUrl="
				+ imageUrl + ", imageSource=" + imageSource + ", plantHealth=" + plantHealth + ", createdDateTime="
				+ createdDateTime + "]";
	}
	
	
	
}
