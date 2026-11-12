package com.devlens.ai.service;

import com.devlens.ai.dto.TestPlanResponse;
import com.devlens.ai.dto.TestRequest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestPlanService {

    public TestPlanResponse generatePlan(TestRequest request) {

        List<String> steps = List.of(
            "Open the provided website",
            "Analyze the login page",
            "Execute the requested test scenarios",
            "Capture any detected failures"
        );

        return new TestPlanResponse(
            request.getWebsiteUrl(),
            request.getInstruction(),
            "DRAFT",
            steps
        );
    }
}