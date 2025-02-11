package iss.nus.edu.sg.sa4106.KebunJio.Services;

import com.mongodb.DuplicateKeyException;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepo;

    // make a new plant
    @Transactional
    public Plant save(Plant newPlant) {
    	return plantRepo.save(newPlant);
    }
    
    // get a plant
    public Optional<Plant> getPlant(String plantId) {
    	return plantRepo.findById(plantId);	
    }
    
    // get all plants owned by a specific user
    public List<Plant> getAllUserPlants(String userId) {
    	return plantRepo.findByUserId(userId);
    }
    
    // update a plant
    public boolean updatePlant(Plant updatedPlant) {
    	// check if it exists
    	if (getPlant(updatedPlant.getId()).isPresent() == false) {
    		return false;
    	}
    	plantRepo.save(updatedPlant);
    	return true;
    }
    
    // delete a plant
    public boolean deletePlant(String plantId) {
    	// check if it exists
    	Optional<Plant> findPlant = getPlant(plantId);
    	if (findPlant.isPresent() == false) {
    		return false;
    	}
    	Plant thePlant = findPlant.get();
    	plantRepo.delete(thePlant);
    	return true;
    }

    
    public long count() {
    	return plantRepo.count();
    }
    
    
    public long harvestedCount(boolean harvested) {
    	return plantRepo.findByHarvested(harvested).size();
    }
    
    
    public long healthCount(String plantHealth) {
    	return plantRepo.findByPlantHealth(plantHealth).size();
    }
    
    
    public List<Plant> getAllPlants() {
    	return plantRepo.findAll();
    }
}
