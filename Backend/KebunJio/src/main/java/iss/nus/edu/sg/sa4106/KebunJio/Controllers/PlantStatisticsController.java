package com.plant.controller;

import com.plant.entity.PlantStatistics;
import com.plant.service.PlantStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true", maxAge = 3600)
@Slf4j
public class PlantStatisticsController {
    private final PlantStatisticsService plantStatisticsService;

    @GetMapping
    public ResponseEntity<PlantStatistics> getLatestStatistics() {
        log.info("接收到获取最新统计数据的请求");
        try {
            PlantStatistics statistics = plantStatisticsService.getLatestStatistics();
            log.info("成功获取统计数据: {}", statistics);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("处理获取统计数据请求时发生错误", e);
            throw e;
        }
    }
} 