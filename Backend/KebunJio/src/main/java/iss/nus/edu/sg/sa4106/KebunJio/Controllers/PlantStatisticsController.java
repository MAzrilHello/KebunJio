//package iss.nus.edu.sg.sa4106.KebunJio.Controllers;
//
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import iss.nus.edu.sg.sa4106.KebunJio.Models.PlantStatistics;
//import iss.nus.edu.sg.sa4106.KebunJio.Services.EventService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//import iss.nus.edu.sg.sa4106.KebunJio.Services.PlantStatisticsService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/statistics")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true", maxAge = 3600)
//@Slf4j
//public class PlantStatisticsController {
//    private final PlantStatisticsService plantStatisticsService;
//
//    @GetMapping
//    public ResponseEntity<PlantStatistics> getLatestStatistics() {
//        log.info("接收到获取最新统计数据的请求");
//        try {
//            PlantStatistics statistics = plantStatisticsService.getLatestStatistics();
//            log.info("成功获取统计数据: {}", statistics);
//            return ResponseEntity.ok(statistics);
//        } catch (Exception e) {
//            log.error("处理获取统计数据请求时发生错误", e);
//            throw e;
//        }
//    }
//} 
