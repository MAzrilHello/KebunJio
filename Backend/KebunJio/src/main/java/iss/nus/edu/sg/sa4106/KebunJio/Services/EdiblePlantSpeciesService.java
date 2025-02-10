package iss.nus.edu.sg.sa4106.KebunJio.Services;


import iss.nus.edu.sg.sa4106.KebunJio.Models.EdiblePlantSpecies;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.EdiblePlantSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EdiblePlantSpeciesService {
    @Autowired
    private EdiblePlantSpeciesRepository ediblePlantSpeciesRepo;

  	//@Autowired
	//private EdiblePlantRepository ediblePlantSpeciesRepository;
  
    // get one species
    //public Optional<EdiblePlantSpecies> getEdiblePlantSpecies(String id) {
    //	return ediblePlantSpeciesRepo.findById(id);
    //}
    
    // get all species
    public List<EdiblePlantSpecies> getAllEdiblePlantSpecies() {
    	return ediblePlantSpeciesRepo.findAll();
    }
	
	public EdiblePlantSpecies getEdiblePlantSpecies(String ediblePlantSpeciesId) {
		System.out.println("id is: "+ediblePlantSpeciesId);
		Optional<EdiblePlantSpecies> ediblePlantSpecies = ediblePlantSpeciesRepo.findById(ediblePlantSpeciesId);
		if(ediblePlantSpecies.isPresent()) {
			System.out.println("is present");
			return ediblePlantSpecies.get();
		}else {
			System.out.println("not present");
			return null;
		}
	}
	
    public EdiblePlantSpecies getEdiblePlantSpeciesByName(String name) {
        return ediblePlantSpeciesRepo.findByName(name);
    }

}
