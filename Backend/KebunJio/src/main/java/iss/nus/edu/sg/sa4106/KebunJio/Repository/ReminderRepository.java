package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Reminder;

public interface ReminderRepository extends MongoRepository<Reminder, String>{
	
	@Query("{'userId': ?0, 'plantId': ?1}")
	List<Reminder> findByUserIdAndPlantId(String userId, String plantId);
	
    @Query("{'id': ?0}")
    Optional<Reminder> findById(String id);

    @Query("{'userId': ?0}")
    List<Reminder> findByUserId(String userId);
    
    @Query("{'plantId': ?0}")
    List<Reminder> findByPlantId(String plantId);
    
    @Query("{'status': ?0}")
    List<Reminder> findByStatus(String status);
    
    @Query("{'reminderType': ?0}")
    List<Reminder> findByReminderType(String reminderType);
    
    @Query("{'reminderDateTime': { $gte: ?0, $lte: ?1 }}")
    List<Reminder> findByReminderDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    boolean existsById(String id);

    void deleteById(String id);

}
