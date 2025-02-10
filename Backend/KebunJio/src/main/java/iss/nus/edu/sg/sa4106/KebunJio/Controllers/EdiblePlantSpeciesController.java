package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.Models.EdiblePlantSpecies;
import iss.nus.edu.sg.sa4106.KebunJio.Services.EdiblePlantSpeciesService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/EdiblePlant")
@CrossOrigin(origins = "*")
public class EdiblePlantSpeciesController {
	@Autowired
	private EdiblePlantSpeciesService ediblePlantSpeciesService;

  
  @GetMapping
	public ResponseEntity<List<EdiblePlantSpecies>> getAllSpecies() {
        try {
        	List<EdiblePlantSpecies> allSpecies = ediblePlantSpeciesService.getAllEdiblePlantSpecies();
            if (allSpecies.size() > 0) {
            	return ResponseEntity.ok(allSpecies);
            } else {
            	return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
	}
  
	  @GetMapping("/byname/{name}")
	  public ResponseEntity<EdiblePlantSpecies> getEdiblePlantSpeciesByName(@PathVariable String name) {
	      // Retrieve the plant by name using the service
	      EdiblePlantSpecies plant = ediblePlantSpeciesService.getEdiblePlantSpeciesByName(name);
	
	      // If the plant is not found, return a 404 response
	      if (plant == null) {
	          return ResponseEntity.notFound().build();
	      }
	
	      // Return the name of the plant as the response
	      return ResponseEntity.ok(plant);
	  }
  
	@GetMapping("/{id}")
	public ResponseEntity<EdiblePlantSpecies> getEdiblePlantSpeciesById(@PathVariable String id) {
		System.out.println("id is: "+id);
		EdiblePlantSpecies ediblePlantSpecies = ediblePlantSpeciesService.getEdiblePlantSpecies(id);
		if (ediblePlantSpecies == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(ediblePlantSpecies);
		}
		
	}
	
}
