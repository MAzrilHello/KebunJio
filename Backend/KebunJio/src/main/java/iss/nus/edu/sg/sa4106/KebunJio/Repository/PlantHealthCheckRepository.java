package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import iss.nus.edu.sg.sa4106.KebunJio.Models.PlantHealthCheckImage;

public interface PlantHealthCheckRepository extends MongoRepository<PlantHealthCheckImage, String>{

}
