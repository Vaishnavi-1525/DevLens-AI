package com.devlens.ai.controller;

import com.devlens.ai.dto.ApiResponse;
import com.devlens.ai.dto.TestPlanResponse;
import com.devlens.ai.dto.TestRequest;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestPlanController {

    @PostMapping("/plan")
    public ApiResponse<TestPlanResponse> generateTestPlan(
            @Valid @RequestBody TestRequest request) {

        List<String> steps = List.of(
            "Open the provided website",
            "Analyze the login page",
            "Execute the requested test scenarios",
            "Capture any detected failures"
        );

        TestPlanResponse response = new TestPlanResponse(
            request.getWebsiteUrl(),
            request.getInstruction(),
            "DRAFT",
            steps
        );

        return new ApiResponse<>(
            true,
            "Test plan generated successfully",
            response
        );
    }
}