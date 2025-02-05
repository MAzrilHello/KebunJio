package iss.nus.edu.sg.sa4106.KebunJio.Services;

import org.springframework.stereotype.Service;

import iss.nus.edu.sg.sa4106.KebunJio.Models.PlantStatistics;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PlantStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantStatisticsService {
    private final PlantStatisticsRepository plantStatisticsRepository;
       
    public PlantStatistics getLatestStatistics() {
        PlantStatistics statistics = plantStatisticsRepository.findTopByOrderByDateDesc();
        System.out.println("Fetched Statistics: " + statistics);
        return statistics;
    }
} 