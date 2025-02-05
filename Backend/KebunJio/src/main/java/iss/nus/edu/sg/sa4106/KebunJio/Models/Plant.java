package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Plant")
public class Plant {
	
	@Id
    private String id;
    private EdiblePlantSpecies ediblePlantSpecies;
    private User user;
    private String name;
    private String disease;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime plantedDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime harvestStartDate;
    private String plantHealth;
    private boolean harvested;
    
    
    public Plant() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EdiblePlantSpecies getEdiblePlantSpecies() {
		return ediblePlantSpecies;
	}

	public void setEdiblePlantSpecies(EdiblePlantSpecies ediblePlantSpecies) {
		this.ediblePlantSpecies = ediblePlantSpecies;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}
    
	public LocalDateTime getPlantedDate() {
		return plantedDate;
	}

	public void setPlantedDate(LocalDateTime plantedDate) {
		this.plantedDate = plantedDate;
	}
    
	public LocalDateTime getHarvestStartDate() {
		return harvestStartDate;
	}

	public void setHarvestStartDate(LocalDateTime harvestStartDate) {
		this.harvestStartDate = harvestStartDate;
	}

	public String getPlantHealth() {
		return plantHealth;
	}

	public void setPlantHealth(String plantHealth) {
		this.plantHealth = plantHealth;
	}
	
	public boolean getHarvested() {
		return harvested;
	}

	public void setHarvested(boolean harvested) {
		this.harvested = harvested;
	}
}
