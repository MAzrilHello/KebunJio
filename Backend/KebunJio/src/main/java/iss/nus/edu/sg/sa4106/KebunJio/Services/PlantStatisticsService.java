package com.plant.service;

import com.plant.entity.PlantStatistics;
import com.plant.repository.PlantStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantStatisticsService {
    private final PlantStatisticsRepository plantStatisticsRepository;
    
    public PlantStatistics getLatestStatistics() {
        log.info("开始获取最新统计数据");
        try {
            return plantStatisticsRepository.findFirstByOrderByDateDesc()
                    .orElseThrow(() -> new RuntimeException("未找到统计数据"));
        } catch (Exception e) {
            log.error("获取统计数据时发生错误: {}", e.getMessage(), e);
            throw new RuntimeException("获取统计数据失败: " + e.getMessage(), e);
        }
    }
} 