package com.devlens.ai.dto;

import java.util.List;

public class TestPlanResponse {

    private String websiteUrl;
    private String instruction;
    private String status;
    private List<TestCase> testCases;

    public TestPlanResponse() {
    }

    public TestPlanResponse(String websiteUrl, String instruction,
                            String status, List<TestCase> testCases) {
        this.websiteUrl = websiteUrl;
        this.instruction = instruction;
        this.status = status;
        this.testCases = testCases;
    }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<TestCase> getTestCases() { return testCases; }
    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
}
