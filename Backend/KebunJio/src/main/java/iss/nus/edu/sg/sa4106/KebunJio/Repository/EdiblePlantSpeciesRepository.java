package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import iss.nus.edu.sg.sa4106.KebunJio.Models.EdiblePlantSpecies;

public interface EdiblePlantSpeciesRepository extends MongoRepository<EdiblePlantSpecies,String> {
	//@Query("{'id': ?0}")
	Optional<EdiblePlantSpecies> findById(String id);
	
	EdiblePlantSpecies findByName(String name);
}
