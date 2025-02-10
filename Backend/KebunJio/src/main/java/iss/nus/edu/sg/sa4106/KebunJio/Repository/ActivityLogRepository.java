package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import iss.nus.edu.sg.sa4106.KebunJio.Models.ActivityLog;

public interface ActivityLogRepository extends MongoRepository<ActivityLog,String> {
	@Query("{'id': ?0}")
	Optional<ActivityLog> findById(String id);
	//@Query("SELECT l FROM ActivityLog l JOIN l.user u WHERE u.id = :userId")
	@Query("{'userId': ?0}")
	List<ActivityLog> findByUserId(@Param("userId") String userId);
	//@Query("SELECT l FROM ActivityLog p JOIN p.user u WHERE p.plantId = :plantId")
	@Query("{'plantId': ?0}")
	List<ActivityLog> findByPlantId(@Param("plantId") String plantId);
}
