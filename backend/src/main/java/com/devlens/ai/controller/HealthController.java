package com.devlens.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> checkHealth() {
        return Map.of(
            "project", "DevLens-AI",
            "status", "UP",
            "message", "DevLens-AI backend is running successfully!"
        );
    }
}