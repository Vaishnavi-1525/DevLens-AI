
package com.devlens.ai.controller;

import com.devlens.ai.automation.TestExecutionResult;
import com.devlens.ai.dto.ApiResponse;
import com.devlens.ai.dto.TestExecutionRequest;
import com.devlens.ai.service.TestExecutionService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/executions")
public class TestExecutionController {

    private final TestExecutionService testExecutionService;

    public TestExecutionController(
            TestExecutionService testExecutionService) {
        this.testExecutionService = testExecutionService;
    }

    // Create a new execution request
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

    // Retrieve all execution records
    @GetMapping
    public ResponseEntity<ApiResponse<List<TestExecutionResult>>> getAllExecutions() {

        List<TestExecutionResult> results =
                testExecutionService.getAllExecutions();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Execution history retrieved successfully",
                        results
                )
        );
    }

    // Retrieve execution by ID
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
