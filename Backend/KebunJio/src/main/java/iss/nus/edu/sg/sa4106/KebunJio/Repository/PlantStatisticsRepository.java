package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;
import iss.nus.edu.sg.sa4106.KebunJio.Models.PlantStatistics;

@Repository
public interface PlantStatisticsRepository extends MongoRepository<PlantStatistics, String> {
    Optional<PlantStatistics> findFirstByOrderByDateDesc();
} 