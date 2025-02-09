package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PlantService;
import iss.nus.edu.sg.sa4106.KebunJio.OtherReusables.Reusables;
import jakarta.servlet.http.HttpSession;

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
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/Plants")
@CrossOrigin(origins = "*")
public class PlantController {
	@Autowired
	private PlantService plantService;
	
	// Must restrict to owner only
	@PostMapping
	public ResponseEntity<Plant> makeNewPlant(@RequestBody Plant plant, HttpSession sessionObj) {
		// first validate that we have permission
		System.out.println("Plant: getting currentUser");
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		// Must restrict to owner only
		System.out.println("Plant: checking permissions");
		HttpStatus editStatus = Reusables.editStatusType(currentUser, plant.getUserId());
		if (editStatus != HttpStatus.OK) {
			return new ResponseEntity<Plant>(editStatus);
		}
		System.out.println("Plant: create new plant");
		try {
			Plant newPlant = new Plant();
			newPlant.setEdiblePlantSpeciesId(plant.getEdiblePlantSpeciesId());
        	newPlant.setUserId(plant.getUserId()); // take user id from the plant object
        	newPlant.setName(plant.getName());
        	newPlant.setDisease(plant.getDisease());
        	newPlant.setPlantedDate(plant.getPlantedDate());
        	newPlant.setHarvestStartDate(plant.getHarvestStartDate());
        	newPlant.setPlantHealth(plant.getPlantHealth());
        	newPlant.setHarvested(plant.getHarvested());
        	return new ResponseEntity<Plant>(plantService.save(newPlant),HttpStatus.CREATED);
		} catch(Exception e) {
			System.out.println(e);
			return ResponseEntity.internalServerError().build();
    	}
	}
	
	// Non-owners can still view.
	@GetMapping("/{plantId}")
	public ResponseEntity<Plant> getPlant(@PathVariable String plantId) {
        try {
            Optional<Plant> existingPlant = plantService.getPlant(plantId);
            return ResponseEntity.ok(existingPlant.get());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
	}
	
	// Non-owners can view list of plants.
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
	
	// Must restrict to owner only
	@PutMapping("/{plantId}")
	public ResponseEntity<Plant> updatePlant(@PathVariable String plantId, @RequestBody Plant plant, HttpSession sessionObj) {
		try {
			Optional<Plant> existingPlant = plantService.getPlant(plantId);
            if (existingPlant.isPresent()) {
            	Plant foundPlant = existingPlant.get();
            	// Must restrict to owner only
            	// first validate that we have permission
        		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
        		HttpStatus editStatus = Reusables.editStatusType(currentUser, foundPlant.getUserId());
        		if (editStatus != HttpStatus.OK) {
        			return new ResponseEntity<Plant>(editStatus);
        		}
        		// do not change the id
            	foundPlant.setEdiblePlantSpeciesId(plant.getEdiblePlantSpeciesId());
            	foundPlant.setUserId(plant.getUserId()); // take user id from the plant object
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
			System.out.println(e);
			return ResponseEntity.internalServerError().build();
		}
	}
	
	
	@DeleteMapping("/{plantId}")
	public ResponseEntity<?> deletePlant(@PathVariable String plantId, HttpSession sessionObj) {
		// first validate that we have permission
		User currentUser = (User) sessionObj.getAttribute("loggedInUser");
		// check if it even exists
		Optional<Plant> existingPlant = plantService.getPlant(plantId);
		if (!existingPlant.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Plant plant = existingPlant.get();
		HttpStatus editStatus = Reusables.editStatusType(currentUser, plant.getUserId());
		if (editStatus != HttpStatus.OK) {
			return new ResponseEntity<>(editStatus);
		}
		
		try {
			if (plantService.deletePlant(plantId)) {
				return ResponseEntity.ok().build();
            } else {
            	return ResponseEntity.notFound().build();
            }
		} catch (RuntimeException e) {
			System.out.println(e);
			return ResponseEntity.internalServerError().build();
		}
	}
}
