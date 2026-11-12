package com.devlens.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @GetMapping("/info")
    public Map<String, String> getProjectInfo() {
        return Map.of(
            "name", "DevLens-AI",
            "version", "1.0.0",
            "description", "AI-powered autonomous website testing and bug analysis platform",
            "backend", "Spring Boot",
            "language", "Java 21"
        );
    }
}