package com.devlens.ai.service;

import com.devlens.ai.automation.TestExecutionResult;
import com.devlens.ai.automation.TestExecutionStatus;
import com.devlens.ai.dto.TestExecutionRequest;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestExecutionService {

    private final Map<String, TestExecutionResult> executions =
            new ConcurrentHashMap<>();

    public TestExecutionResult createExecution(
            TestExecutionRequest request) {

        TestExecutionResult result = new TestExecutionResult();

        result.setExecutionId(UUID.randomUUID().toString());
        result.setWebsiteUrl(request.getWebsiteUrl());
        result.setStatus(TestExecutionStatus.PENDING);

        result.setTotalTests(request.getApprovedSteps().size());
        result.setPassedTests(0);
        result.setFailedTests(0);

        result.setSummary(
            "Execution request accepted. Waiting for browser automation."
        );

        executions.put(result.getExecutionId(), result);

        return result;
    }

    public TestExecutionResult getExecution(String executionId) {

        return executions.get(executionId);
    }
}