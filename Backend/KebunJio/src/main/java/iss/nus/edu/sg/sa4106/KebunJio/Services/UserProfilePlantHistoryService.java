package iss.nus.edu.sg.sa4106.KebunJio.Services;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PlantHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfilePlantHistoryService {

    @Autowired
    private PlantHistoryRepository plantHistoryRepository;


    public List<Plant> getPlantsByUserId(String userId){
        return plantHistoryRepository.findByUserId(userId);
    }
}
