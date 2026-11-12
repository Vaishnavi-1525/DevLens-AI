package com.devlens.ai.dto;

import java.util.List;

public class TestPlanResponse {

    private String websiteUrl;
    private String instruction;
    private String status;
    private List<String> testSteps;

    public TestPlanResponse() {
    }

    public TestPlanResponse(
            String websiteUrl,
            String instruction,
            String status,
            List<String> testSteps) {

        this.websiteUrl = websiteUrl;
        this.instruction = instruction;
        this.status = status;
        this.testSteps = testSteps;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(List<String> testSteps) {
        this.testSteps = testSteps;
    }
}