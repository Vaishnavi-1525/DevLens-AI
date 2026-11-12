package com.devlens.ai.controller;

import com.devlens.ai.dto.ApiResponse;
import com.devlens.ai.dto.TestPlanResponse;
import com.devlens.ai.dto.TestRequest;
import com.devlens.ai.service.TestPlanService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tests")
public class TestPlanController {

    private final TestPlanService testPlanService;

    public TestPlanController(TestPlanService testPlanService) {
        this.testPlanService = testPlanService;
    }

    @PostMapping("/plan")
    public ApiResponse<TestPlanResponse> generateTestPlan(
            @Valid @RequestBody TestRequest request) {

        TestPlanResponse response =
                testPlanService.generatePlan(request);

        return new ApiResponse<>(
            true,
            "Test plan generated successfully",
            response
        );
    }
}