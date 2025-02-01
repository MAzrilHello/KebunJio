package iss.nus.edu.sg.sa4106.KebunJio.Models;

public class Plant {
	@id
	priavte String id;

    private EdiblePlantSpecies ediblePlantSpecies;
    private User user;
    private String name;
    
    public Plant() {}

	public int getPlantId() {
		return id;
	}

	public void setPlantId(int plantId) {
		this.plantId = id;
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
