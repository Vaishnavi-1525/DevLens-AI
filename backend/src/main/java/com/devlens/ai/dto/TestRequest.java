package com.devlens.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TestRequest {

    @NotBlank(message = "Website URL is required")
    private String websiteUrl;

    @NotBlank(message = "Testing instruction is required")
    @Size(max = 2000, message = "Instruction cannot exceed 2000 characters")
    private String instruction;

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
}