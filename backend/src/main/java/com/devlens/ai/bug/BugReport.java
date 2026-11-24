package com.devlens.ai.bug;

import java.time.LocalDateTime;

public class BugReport {

    private String bugId;
    private String title;
    private String description;
    private String failedTestName;
    private String errorDetails;
    private String websiteUrl;
    private String executionId;

    private String status;
    private String priority;
    private String bugType;
    private String severity;
    private String injectedPhase;

    private String screenshotPath;
    private String screenshotError;
    private String screenshotFor;
    private LocalDateTime screenshotCapturedAt;

    private String tracePath;

    private String browser;
    private String pageUrl;
    private LocalDateTime reportedAt;

    public BugReport() {
    }

    public String getBugId() { return bugId; }
    public void setBugId(String bugId) { this.bugId = bugId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFailedTestName() { return failedTestName; }
    public void setFailedTestName(String failedTestName) { this.failedTestName = failedTestName; }

    public String getErrorDetails() { return errorDetails; }
    public void setErrorDetails(String errorDetails) { this.errorDetails = errorDetails; }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }

    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getBugType() { return bugType; }
    public void setBugType(String bugType) { this.bugType = bugType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getInjectedPhase() { return injectedPhase; }
    public void setInjectedPhase(String injectedPhase) { this.injectedPhase = injectedPhase; }

    public String getScreenshotPath() { return screenshotPath; }
    public void setScreenshotPath(String screenshotPath) { this.screenshotPath = screenshotPath; }

    public String getScreenshotError() { return screenshotError; }
    public void setScreenshotError(String screenshotError) { this.screenshotError = screenshotError; }

    public String getScreenshotFor() { return screenshotFor; }
    public void setScreenshotFor(String screenshotFor) { this.screenshotFor = screenshotFor; }

    public LocalDateTime getScreenshotCapturedAt() { return screenshotCapturedAt; }
    public void setScreenshotCapturedAt(LocalDateTime screenshotCapturedAt) {
        this.screenshotCapturedAt = screenshotCapturedAt;
    }

    public String getTracePath() { return tracePath; }
    public void setTracePath(String tracePath) { this.tracePath = tracePath; }

    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }

    public String getPageUrl() { return pageUrl; }
    public void setPageUrl(String pageUrl) { this.pageUrl = pageUrl; }

    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
}