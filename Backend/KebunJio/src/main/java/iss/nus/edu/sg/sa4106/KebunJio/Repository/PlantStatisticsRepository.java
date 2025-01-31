package com.plant.repository;

import com.plant.entity.PlantStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PlantStatisticsRepository extends MongoRepository<PlantStatistics, String> {
    Optional<PlantStatistics> findFirstByOrderByDateDesc();
} 