package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import iss.nus.edu.sg.sa4106.KebunJio.Models.EdiblePlantSpecies;

public interface EdiblePlantRepository extends MongoRepository<EdiblePlantSpecies,String> {
	
    @Query("{'_id': ?0}") 
    Optional findById(String id);
    
    EdiblePlantSpecies findByName(String name);
}
