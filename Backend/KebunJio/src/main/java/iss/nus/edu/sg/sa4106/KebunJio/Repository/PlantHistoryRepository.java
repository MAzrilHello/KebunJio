package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlantHistoryRepository extends MongoRepository<Plant, String> {
    List<Plant> findByUser(User user);
}

