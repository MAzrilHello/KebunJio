package iss.nus.edu.sg.sa4106.KebunJio.Models;

public class Plant {
    private int plantId;
    private EdiblePlantSpecies ediblePlantSpecies;
    private User user;
    private String name;
    
    public Plant() {}

	public int getPlantId() {
		return plantId;
	}

	public void setPlantId(int plantId) {
		this.plantId = plantId;
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
