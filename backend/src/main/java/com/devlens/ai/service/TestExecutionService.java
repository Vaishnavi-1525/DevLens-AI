package com.devlens.ai.service;

import com.devlens.ai.automation.PlaywrightService;
import com.devlens.ai.automation.TestExecutionResult;
import com.devlens.ai.automation.TestExecutionStatus;
import com.devlens.ai.dto.TestExecutionRequest;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestExecutionService {

    private final Map<String, TestExecutionResult> executions =
            new ConcurrentHashMap<>();

    private final PlaywrightService playwrightService;

    public TestExecutionService(PlaywrightService playwrightService) {
        this.playwrightService = playwrightService;
    }

    public TestExecutionResult createExecution(
            TestExecutionRequest request) {

        TestExecutionResult result = new TestExecutionResult();

        result.setExecutionId(UUID.randomUUID().toString());
        result.setWebsiteUrl(request.getWebsiteUrl());
        result.setApprovedSteps(request.getApprovedSteps());
        result.setStatus(TestExecutionStatus.RUNNING);
        result.setStartedAt(LocalDateTime.now());

        executions.put(result.getExecutionId(), result);

        try {
            Map<String, Object> browserResult =
                    playwrightService.executeWebsiteChecks(
                            request.getWebsiteUrl()
                    );

            int total = (int) browserResult.get("totalTests");
            int passed = (int) browserResult.get("passedTests");
            int failed = (int) browserResult.get("failedTests");

            result.setTotalTests(total);
            result.setPassedTests(passed);
            result.setFailedTests(failed);

            result.setStatus(
                    failed == 0
                            ? TestExecutionStatus.PASSED
                            : TestExecutionStatus.FAILED
            );

            result.setSummary(
                    "Browser execution completed. "
                    + passed + " checks passed, "
                    + failed + " checks failed."
            );

        } catch (Exception exception) {

            result.setStatus(TestExecutionStatus.FAILED);
            result.setFailedTests(1);
            result.setSummary(
                    "Browser execution failed: " + exception.getMessage()
            );
        }

        result.setCompletedAt(LocalDateTime.now());

        return result;
    }

    public List<TestExecutionResult> getAllExecutions() {
        return new ArrayList<>(executions.values());
    }

    public TestExecutionResult getExecution(String executionId) {
        return executions.get(executionId);
    }
}