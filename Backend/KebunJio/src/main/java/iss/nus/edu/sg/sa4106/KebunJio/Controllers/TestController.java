package com.plant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint is working!");
    }
    
    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Root endpoint is working!");
    }
} 