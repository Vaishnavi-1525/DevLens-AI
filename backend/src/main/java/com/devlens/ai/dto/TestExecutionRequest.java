package com.devlens.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class TestExecutionRequest {

    @NotBlank(message = "Website URL is required")
    private String websiteUrl;

    @NotEmpty(message = "At least one approved test step is required")
    @Size(max = 20, message = "Maximum 20 test steps are allowed")
    private List<@NotBlank String> approvedSteps;

    public TestExecutionRequest() {
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public List<String> getApprovedSteps() {
        return approvedSteps;
    }

    public void setApprovedSteps(List<String> approvedSteps) {
        this.approvedSteps = approvedSteps;
    }
}