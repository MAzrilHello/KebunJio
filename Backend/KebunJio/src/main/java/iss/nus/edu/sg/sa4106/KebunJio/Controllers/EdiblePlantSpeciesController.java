package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iss.nus.edu.sg.sa4106.KebunJio.Models.EdiblePlantSpecies;
import iss.nus.edu.sg.sa4106.KebunJio.Services.EdiblePlantSpeciesService;

@RestController
@RequestMapping("/EdiblePlant")
@CrossOrigin(origins = "*")
public class EdiblePlantSpeciesController {
	@Autowired
	private EdiblePlantSpeciesService ediblePlantSpeciesService;
	
	@GetMapping("/{id}/")
	public ResponseEntity getEdiblePlantSpeciesById(@PathVariable String id) {
		List<EdiblePlantSpecies> ediblePlantSpeciesList = (List<EdiblePlantSpecies>) ediblePlantSpeciesService.getEdiblePlantSpeciesByEdiblePlantSpeciesId(id);
		return new ResponseEntity<>(ediblePlantSpeciesList,HttpStatus.OK);
	}
	
    @GetMapping("/{name}")
    public ResponseEntity<String> getEdiblePlantSpeciesByName(@PathVariable String name) {
        // Retrieve the plant by name using the service
        EdiblePlantSpecies plant = ediblePlantSpeciesService.getEdiblePlantSpeciesByName(name);

        // If the plant is not found, return a 404 response
        if (plant == null) {
            return ResponseEntity.notFound().build();
        }

        // Return the name of the plant as the response
        return ResponseEntity.ok(plant.getName());
    }

}
