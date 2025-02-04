package iss.nus.edu.sg.sa4106.KebunJio.Models;

import org.springframework.data.annotation.Id;

public class Plant {

	@Id
	private String id;

	private EdiblePlantSpecies ediblePlantSpecies;
	private User user;
	private String name;

	public Plant() {}

	public String getPlantId() {
		return id;
	}

	public void setPlantId(String plantId) {
		this.id = plantId;
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



}
