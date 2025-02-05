package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PlantService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Plants")
@CrossOrigin(origins = "*")
public class PlantController {
	@Autowired
	private PlantService plantService;
	
	
	@PostMapping
	public Plant makeNewPlant(@RequestBody Plant plant) {
		return plantService.save(plant);
	}
	
	@GetMapping("/{plantId}")
	public ResponseEntity<Plant> getPlant(@PathVariable String plantId) {
        try {
            Optional<Plant> existingPlant = plantService.getPlant(plantId);
            return ResponseEntity.ok(existingPlant.get());
            //if (existingPlant.isPresent()) {
            //	
            //} else {
            //	return ResponseEntity.notFound().build();
            //}
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
	}
	
	@GetMapping("/Users/{userId}")
	public ResponseEntity<List<Plant>> getUserPlants(@PathVariable String userId) {
        try {
        	List<Plant> userPlants = plantService.getAllUserPlants(userId);
        	return ResponseEntity.ok(userPlants);
            //if (userPlants.size() > 0) {
            //	return ResponseEntity.ok(userPlants);
            //} else {
            //	return ResponseEntity.notFound().build();
            //}
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
	}
	
	@PutMapping("/{plantId}")
	public ResponseEntity<Plant> updatePlant(@PathVariable String plantId, @RequestBody Plant plant) {
		try {
			Optional<Plant> existingPlant = plantService.getPlant(plantId);
            if (existingPlant.isPresent()) {
            	Plant foundPlant = existingPlant.get();
            	foundPlant.setEdiblePlantSpecies(plant.getEdiblePlantSpecies());
            	foundPlant.setUser(plant.getUser());
            	foundPlant.setName(plant.getName());
            	foundPlant.setDisease(plant.getDisease());
            	foundPlant.setPlantedDate(plant.getPlantedDate());
            	foundPlant.setHarvestStartDate(plant.getHarvestStartDate());
            	foundPlant.setPlantHealth(plant.getPlantHealth());
            	foundPlant.setHarvested(plant.getHarvested());
            	return ResponseEntity.ok(plantService.save(foundPlant));
            } else {
            	return ResponseEntity.notFound().build();
            }
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@DeleteMapping("/{plantId}")
	public ResponseEntity<?> deletePlant(@PathVariable String plantId) {
		try {
			if (plantService.deletePlant(plantId)) {
				return ResponseEntity.ok().build();
            } else {
            	return ResponseEntity.notFound().build();
            }
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
