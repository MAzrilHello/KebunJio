package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;

public interface PlantRepository extends MongoRepository<Plant,String> {
	@Query("{'id': ?0}")
	Optional<Plant> findById(String id);
	//@Query("SELECT p FROM Plant p JOIN p.user u WHERE u.id = :userId")
	@Query("{'userId': ?0}")
	List<Plant> findByUserId(@Param("userId") String userId);
	
	@Query("{'harvested': ?0}")
	List<Plant> findByHarvested(@Param("harvested") boolean harvested);
	
	@Query("{'plantHealth': ?0}")
	List<Plant> findByPlantHealth(@Param("plantHealth") String plantHealth);
}
