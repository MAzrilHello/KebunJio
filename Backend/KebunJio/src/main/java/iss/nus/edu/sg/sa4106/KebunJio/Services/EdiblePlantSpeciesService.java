package iss.nus.edu.sg.sa4106.KebunJio.Services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import iss.nus.edu.sg.sa4106.KebunJio.Models.EdiblePlantSpecies;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.EdiblePlantRepository;

@Service
public class EdiblePlantSpeciesService {
	
	@Autowired
	private EdiblePlantRepository ediblePlantSpeciesRepository;
	
	public EdiblePlantSpecies getEdiblePlantSpeciesByEdiblePlantSpeciesId(String EdiblePlantSpeciesId) {
		Optional<EdiblePlantSpecies> ediblePlantSpecies = ediblePlantSpeciesRepository.findById(EdiblePlantSpeciesId);
		if(ediblePlantSpecies.isPresent()) {
			return ediblePlantSpecies.get();
		}else {
			return null;
		}
	}
	
    public EdiblePlantSpecies getEdiblePlantSpeciesByName(String name) {
        return ediblePlantSpeciesRepository.findByName(name);
    }

}
