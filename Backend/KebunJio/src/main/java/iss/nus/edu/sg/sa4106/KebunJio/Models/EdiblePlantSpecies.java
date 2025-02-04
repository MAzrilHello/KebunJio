package iss.nus.edu.sg.sa4106.KebunJio.Models;

import org.springframework.data.annotation.Id;

public class EdiblePlantSpecies {
	@Id
	private String id;

	private String name;
	private String scientificName;
	private String description;
	private String wateringTips;
	private String sunlight;
	private String soilType;
	private String harvestTime;
	private String commonPests;
	private String growingSpace;
	private String fertilizerTips;
	private String specialNeeds;
	private String imageUrl;

	public EdiblePlantSpecies() {}

	public String getEdiblePlantSpeciesId() {
		return id;
	}

	public void setEdiblePlantSpeciesId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWateringTips() {
		return wateringTips;
	}

	public void setWateringTips(String wateringTips) {
		this.wateringTips = wateringTips;
	}

	public String getSunlight() {
		return sunlight;
	}

	public void setSunlight(String sunlight) {
		this.sunlight = sunlight;
	}

	public String getSoilType() {
		return soilType;
	}

	public void setSoilType(String soilType) {
		this.soilType = soilType;
	}

	public String getHarvestTime() {
		return harvestTime;
	}

	public void setHarvestTime(String harvestTime) {
		this.harvestTime = harvestTime;
	}

	public String getCommonPests() {
		return commonPests;
	}

	public void setCommonPests(String commonPests) {
		this.commonPests = commonPests;
	}

	public String getGrowingSpace() {
		return growingSpace;
	}

	public void setGrowingSpace(String growingSpace) {
		this.growingSpace = growingSpace;
	}

	public String getFertilizerTips() {
		return fertilizerTips;
	}

	public void setFertilizerTips(String fertilizerTips) {
		this.fertilizerTips = fertilizerTips;
	}

	public String getSpecialNeeds() {
		return specialNeeds;
	}

	public void setSpecialNeeds(String specialNeeds) {
		this.specialNeeds = specialNeeds;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



}
