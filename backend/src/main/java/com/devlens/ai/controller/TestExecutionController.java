package com.devlens.ai.controller;

import com.devlens.ai.automation.TestExecutionResult;
import com.devlens.ai.dto.ApiResponse;
import com.devlens.ai.dto.TestExecutionRequest;
import com.devlens.ai.service.TestExecutionService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/executions")
public class TestExecutionController {

    private final TestExecutionService testExecutionService;

    public TestExecutionController(
            TestExecutionService testExecutionService) {
        this.testExecutionService = testExecutionService;
    }

    @PostMapping
    public ApiResponse<TestExecutionResult> createExecution(
            @Valid @RequestBody TestExecutionRequest request) {

        TestExecutionResult result =
                testExecutionService.createExecution(request);

        return new ApiResponse<>(
                true,
                "Test execution request created successfully",
                result
        );
    }

    @GetMapping("/{executionId}")
    public ResponseEntity<ApiResponse<TestExecutionResult>> getExecution(
            @PathVariable String executionId) {

        TestExecutionResult result =
                testExecutionService.getExecution(executionId);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Execution details retrieved successfully",
                        result
                )
        );
    }
}