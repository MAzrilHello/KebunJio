package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalUser", 4);
        statistics.put("totalPlanted", 10);
        statistics.put("totalHarvested", 8);
        statistics.put("totalDisease", 2);
        return statistics;
    }
} 