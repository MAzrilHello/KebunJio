package iss.nus.edu.sg.sa4106.KebunJio.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iss.nus.edu.sg.sa4106.KebunJio.Models.PlantHealthCheckImage;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PlantHealthCheckRepository;

@Service
public class PlantHealthCheckService {
	
    @Autowired
    private PlantHealthCheckRepository plantHealthCheckRepository;

    public PlantHealthCheckImage saveHealthCheck(PlantHealthCheckImage healthCheck) {
        return plantHealthCheckRepository.save(healthCheck);
    }

    public Optional<PlantHealthCheckImage> getHealthCheckById(String id) {
        return plantHealthCheckRepository.findById(id);
    }

    public List<PlantHealthCheckImage> getAllHealthChecks() {
        return plantHealthCheckRepository.findAll();
    }

}
