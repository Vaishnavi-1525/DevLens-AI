
package com.devlens.ai.bug;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BugReportService {

    private static final Set<String> ALLOWED_PHASES = Set.of(
            "Requirements",
            "Design",
            "Coding",
            "Deployment",
            "Bug Leakage"
    );

    private final Map<String, BugReport> bugReports =
            new ConcurrentHashMap<>();

    public List<BugReport> createReports(
            String executionId,
            String websiteUrl,
            List<Map<String, String>> checks) {

        List<BugReport> createdReports = new ArrayList<>();

        for (Map<String, String> check : checks) {

            if (!"FAILED".equals(check.get("status"))) {
                continue;
            }

            BugReport report = new BugReport();

            String bugId = "BUG-" +
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();

            String testName = check.get("name");
            String errorDetails = check.get("details");

            report.setBugId(bugId);
            report.setTitle(
                    testName + " failed on " + websiteUrl
            );

            report.setDescription(
                    "A test failure was detected during automated "
                    + "website testing.\n\n"
                    + "Failed Test: " + testName + "\n"
                    + "Website: " + websiteUrl + "\n"
                    + "Execution ID: " + executionId
            );

            report.setFailedTestName(testName);
            report.setErrorDetails(errorDetails);
            report.setWebsiteUrl(websiteUrl);
            report.setExecutionId(executionId);

            report.setStatus("OPEN");
            report.setPriority(
                    determinePriority(testName, errorDetails)
            );
            report.setSeverity(
                    determineSeverity(testName, errorDetails)
            );
            report.setBugType(
                    determineBugType(testName, errorDetails)
            );

            report.setInjectedPhase("Not Determined");

            // Screenshot evidence
            report.setScreenshotPath(
                    check.get("screenshotPath")
            );

            report.setScreenshotError(
                    check.get("screenshotError")
            );

            report.setScreenshotFor(
                    check.get("screenshotPath") != null
                            ? testName
                            : null
            );

            report.setScreenshotCapturedAt(
                    check.get("screenshotPath") != null
                            ? LocalDateTime.now()
                            : null
            );

            // Playwright Trace ZIP evidence
            report.setTracePath(
                    check.get("tracePath")
            );

            report.setBrowser("Chromium");
            report.setPageUrl(websiteUrl);
            report.setReportedAt(LocalDateTime.now());

            bugReports.put(bugId, report);
            createdReports.add(report);
        }

        return createdReports;
    }

    public List<BugReport> getAllReports() {
        return new ArrayList<>(bugReports.values());
    }

    public BugReport getReport(String bugId) {
        return bugReports.get(bugId);
    }

    public BugReport updateInjectedPhase(
            String bugId,
            String injectedPhase) {

        if (!ALLOWED_PHASES.contains(injectedPhase)) {
            throw new IllegalArgumentException(
                    "Invalid injected phase: " + injectedPhase
            );
        }

        BugReport report = bugReports.get(bugId);

        if (report == null) {
            return null;
        }

        report.setInjectedPhase(injectedPhase);

        return report;
    }

    // Replace screenshot path for an existing bug report.
    public BugReport updateScreenshotPath(
            String bugId,
            String screenshotPath) {

        BugReport report = bugReports.get(bugId);

        if (report == null) {
            return null;
        }

        report.setScreenshotPath(screenshotPath);
        report.setScreenshotError(null);
        report.setScreenshotFor(report.getFailedTestName());
        report.setScreenshotCapturedAt(LocalDateTime.now());

        return report;
    }

    private String determinePriority(
            String testName,
            String details) {

        String error = details == null
                ? ""
                : details.toLowerCase();

        if (error.contains("http 5")
                || error.contains("server error")) {
            return "CRITICAL";
        }

        if (error.contains("http 4")
                || error.contains("timeout")
                || error.contains("navigation failed")) {
            return "HIGH";
        }

        if ("Website response".equals(testName)) {
            return "HIGH";
        }

        return "MEDIUM";
    }

    private String determineSeverity(
            String testName,
            String details) {

        String error = details == null
                ? ""
                : details.toLowerCase();

        if (error.contains("http 5")
                || error.contains("server error")) {
            return "CRITICAL";
        }

        if (error.contains("http 4")
                || error.contains("timeout")) {
            return "MAJOR";
        }

        if ("Website response".equals(testName)) {
            return "MAJOR";
        }

        return "MINOR";
    }

    private String determineBugType(
            String testName,
            String details) {

        String error = details == null
                ? ""
                : details.toLowerCase();

        if (error.contains("browser automation error")
                || error.contains("playwright")) {
            return "Browser / Automation";
        }

        if (error.contains("timeout")) {
            return "Performance / Availability";
        }

        if ("Website response".equals(testName)) {
            return "Availability";
        }

        if ("Page title".equals(testName)) {
            return "UI / Content";
        }

        if ("Page content".equals(testName)) {
            return "Content / Visibility";
        }

        return "Functional";
    }
}
