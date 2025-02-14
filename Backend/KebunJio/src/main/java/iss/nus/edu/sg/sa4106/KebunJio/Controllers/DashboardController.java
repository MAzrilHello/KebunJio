package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iss.nus.edu.sg.sa4106.KebunJio.Models.EdiblePlantSpecies;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Services.EdiblePlantSpeciesService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PlantService;
import iss.nus.edu.sg.sa4106.KebunJio.Services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Dashboard")
public class DashboardController {

	@Autowired
    private UserService userService;
	
	@Autowired
    private PlantService plantService;
	
	@Autowired
	private EdiblePlantSpeciesService ediblePlantSpeciesService;
	
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalUser", 4);
        statistics.put("totalPlanted", 10);
        statistics.put("totalHarvested", 8);
        statistics.put("totalDisease", 2);
        return statistics;
    }
    
    
    @GetMapping("/DataSummary")
    public Map<String, Object> getDataSummary() {
    	Map<String,Object> dataSummary = new HashMap<>();
    	
    	dataSummary.put("totalUser",userService.count());
    	dataSummary.put("totalPlanted", plantService.count());
    	dataSummary.put("totalHarvested", plantService.harvestedCount(true));
    	dataSummary.put("totalDisease", plantService.healthCount("Not Healthy"));
    	dataSummary.put("totalHealthy", plantService.healthCount("Healthy"));
    	
    	Map<String,Object> plantTypeCount = new HashMap<>();
    	Map<String,String> speciesIdToName = new HashMap<>();
    	
    	List<EdiblePlantSpecies> speciesList = ediblePlantSpeciesService.getAllEdiblePlantSpecies();
    	for (int i = 0; i < speciesList.size(); i++) {
    		EdiblePlantSpecies currentSpecies = speciesList.get(i);
    		String speciesId = currentSpecies.getId();
    		String speciesName = currentSpecies.getName();
    		String scientificName = currentSpecies.getScientificName();
    		String fullName = speciesName + " (" + scientificName + ")";
    		speciesIdToName.put(speciesId, fullName);
    	}
    	
    	List<Plant> plantList = plantService.getAllPlants();
    	
    	for (int i = 0; i < plantList.size(); i++) {
    		Plant currentPlant = plantList.get(i);
    		String plantId = currentPlant.getId();
    		int thisCount = 0;
    		if (plantTypeCount.containsKey(plantId)) {
    			thisCount = (int) plantTypeCount.get(plantId);
    		}
    		plantTypeCount.put(plantId, thisCount+1);
    	}
    	
    	dataSummary.put("plantTypeCount",plantTypeCount);
    	dataSummary.put("speciesIdToName", speciesIdToName);
    	
    	return dataSummary;
    }
} 