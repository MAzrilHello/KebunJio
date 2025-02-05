package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import iss.nus.edu.sg.sa4106.KebunJio.Models.PlantStatistics;
import iss.nus.edu.sg.sa4106.KebunJio.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PlantStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class PlantStatisticsController {
    private final PlantStatisticsService plantStatisticsService;

    @GetMapping
    public ResponseEntity<PlantStatistics> getLatestStatistics() {
        try {
            // Fetch the latest statistics from the service (which interacts with MongoDB)
            PlantStatistics statistics = plantStatisticsService.getLatestStatistics();
            
            if (statistics == null) {
                return ResponseEntity.status(404).body(null);  // Return 404 if no data is found
            }
            
            // Return the fetched statistics in the response
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            // Handle error, if any
            return ResponseEntity.status(500).body(null);
        }
    }
} 