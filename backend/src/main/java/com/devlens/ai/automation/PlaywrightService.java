
package com.devlens.ai.automation;

import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PlaywrightService {

    @Value("${devlens.screenshots.directory:${user.home}/.devlens-ai/screenshots}")
    private String screenshotDirectory;

    @Value("${devlens.traces.directory:${user.home}/.devlens-ai/traces}")
    private String traceDirectory;

    public Map<String, Object> executeWebsiteChecks(String websiteUrl) {

        List<Map<String, String>> checks = new ArrayList<>();

        String traceFileName = UUID.randomUUID() + ".zip";
        Path traceFile = null;

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            try {
                Page page = browser.newPage();

                Tracing tracing = page.context().tracing();
                boolean tracingStarted = false;

                try {
                    // Start Playwright tracing before navigation.
                    try {
                        Path directory = Path.of(traceDirectory)
                                .toAbsolutePath()
                                .normalize();

                        Files.createDirectories(directory);

                        traceFile = directory.resolve(traceFileName);

                        tracing.start(
                                new Tracing.StartOptions()
                                        .setScreenshots(true)
                                        .setSnapshots(true)
                                        .setSources(true)
                        );

                        tracingStarted = true;

                        System.out.println(
                                "PLAYWRIGHT TRACE STARTED"
                        );

                    } catch (Exception traceException) {
                        System.err.println(
                                "TRACE START FAILED: "
                                        + traceException.getMessage()
                        );
                    }

                    Response response = null;
                    String navigationError = null;

                    try {
                        response = page.navigate(
                                websiteUrl,
                                new Page.NavigateOptions()
                                        .setTimeout(30000)
                        );
                    } catch (Exception exception) {
                        navigationError = describeFailure(exception);
                    }

                    // Check 1: Website response
                    if (navigationError != null) {

                        addCheckWithScreenshot(
                                checks, page, "Website response",
                                false, navigationError
                        );

                    } else {

                        int httpStatus = response != null
                                ? response.status()
                                : -1;

                        boolean pageLoaded =
                                httpStatus >= 200 && httpStatus < 400;

                        addCheckWithScreenshot(
                                checks, page, "Website response",
                                pageLoaded, describeHttpStatus(httpStatus)
                        );
                    }

                    // Check 2: Page title
                    if (navigationError != null) {

                        addSkippedCheck(
                                checks, "Page title",
                                "Skipped because website navigation failed. "
                                        + navigationError
                        );

                    } else {

                        try {
                            String title = page.title();

                            boolean titlePresent =
                                    title != null && !title.isBlank();

                            addCheckWithScreenshot(
                                    checks, page, "Page title",
                                    titlePresent,
                                    titlePresent
                                            ? "Page title: " + title
                                            : "FAILED: Page title is empty."
                            );

                        } catch (Exception exception) {

                            addCheckWithScreenshot(
                                    checks, page, "Page title",
                                    false, describeFailure(exception)
                            );
                        }
                    }

                    // Check 3: Page content
                    if (navigationError != null) {

                        addSkippedCheck(
                                checks, "Page content",
                                "Skipped because website navigation failed. "
                                        + navigationError
                        );

                    } else {

                        try {
                            String bodyText =
                                    page.locator("body").innerText();

                            boolean contentPresent =
                                    page.locator("body").isVisible()
                                    && bodyText != null
                                    && !bodyText.isBlank();

                            addCheckWithScreenshot(
                                    checks, page, "Page content",
                                    contentPresent,
                                    contentPresent
                                            ? "Visible page content found."
                                            : "FAILED: Page content is empty or not visible."
                            );

                        } catch (Exception exception) {

                            addCheckWithScreenshot(
                                    checks, page, "Page content",
                                    false, describeFailure(exception)
                            );
                        }
                    }

                } finally {

                    // Stop tracing before closing the browser.
                    if (tracingStarted) {

                        try {
                            tracing.stop(
                                    new Tracing.StopOptions()
                                            .setPath(traceFile)
                            );

                            if (Files.exists(traceFile)) {

                                System.out.println(
                                        "TRACE SAVED: " + traceFile
                                );

                                // Attach trace filename to every failed check.
                                for (Map<String, String> check : checks) {

                                    if ("FAILED".equals(
                                            check.get("status"))) {

                                        check.put(
                                                "tracePath",
                                                traceFileName
                                        );
                                    }
                                }

                            } else {
                                throw new IllegalStateException(
                                        "Trace ZIP was not created."
                                );
                            }

                        } catch (Exception traceException) {

                            System.err.println(
                                    "TRACE SAVE FAILED: "
                                            + traceException.getMessage()
                            );

                            for (Map<String, String> check : checks) {

                                if ("FAILED".equals(
                                        check.get("status"))) {

                                    check.put(
                                            "traceError",
                                            traceException.getMessage()
                                    );
                                }
                            }
                        }
                    }

                    browser.close();
                }

            } finally {
                if (browser.isConnected()) {
                    browser.close();
                }
            }

        } catch (Exception exception) {

            addCheck(
                    checks, "Browser execution",
                    false, describeFailure(exception)
            );
        }

        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (Map<String, String> check : checks) {

            switch (check.get("status")) {
                case "PASSED" -> passed++;
                case "FAILED" -> failed++;
                case "SKIPPED" -> skipped++;
                default -> {
                }
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("totalTests", checks.size());
        result.put("passedTests", passed);
        result.put("failedTests", failed);
        result.put("skippedTests", skipped);
        result.put("checks", checks);

        return result;
    }

    private void addCheckWithScreenshot(
            List<Map<String, String>> checks,
            Page page,
            String name,
            boolean success,
            String details) {

        addCheck(checks, name, success, details);

        if (!success) {
            Map<String, String> check =
                    checks.get(checks.size() - 1);

            captureFailureScreenshot(page, check);
        }
    }

    private void addCheck(
            List<Map<String, String>> checks,
            String name,
            boolean success,
            String details) {

        Map<String, String> check = new LinkedHashMap<>();

        check.put("name", name);
        check.put("status", success ? "PASSED" : "FAILED");
        check.put("details", details);

        checks.add(check);
    }

    private void addSkippedCheck(
            List<Map<String, String>> checks,
            String name,
            String details) {

        Map<String, String> check = new LinkedHashMap<>();

        check.put("name", name);
        check.put("status", "SKIPPED");
        check.put("details", details);

        checks.add(check);
    }

    private void captureFailureScreenshot(
            Page page,
            Map<String, String> check) {

        if (page == null) {
            check.put(
                    "screenshotError",
                    "Browser page is unavailable."
            );
            return;
        }

        try {
            Path directory = Path.of(screenshotDirectory)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(directory);

            String fileName = UUID.randomUUID() + ".png";

            Path screenshotFile = directory.resolve(fileName);

            page.screenshot(
                    new Page.ScreenshotOptions()
                            .setPath(screenshotFile)
                            .setFullPage(true)
                            .setTimeout(15000)
            );

            if (!Files.exists(screenshotFile)) {
                throw new IllegalStateException(
                        "Screenshot file was not created."
                );
            }

            check.put(
                    "screenshotPath",
                    "/screenshots/" + fileName
            );

            check.put(
                    "screenshotFor",
                    check.get("name")
            );

            check.put(
                    "screenshotCapturedAt",
                    LocalDateTime.now().toString()
            );

            System.out.println(
                    "SCREENSHOT SAVED: " + screenshotFile
            );

        } catch (Exception exception) {

            exception.printStackTrace();

            check.put(
                    "screenshotError",
                    "Screenshot capture failed: "
                            + exception.getMessage()
            );
        }
    }

    private String describeHttpStatus(int status) {

        if (status >= 200 && status < 300) {
            return "HTTP " + status + ": Successful response.";
        }

        if (status >= 300 && status < 400) {
            return "HTTP " + status + ": Redirection response.";
        }

        if (status == 404) {
            return "HTTP 404: Page not found.";
        }

        if (status >= 400 && status < 500) {
            return "HTTP " + status + ": Client error.";
        }

        if (status >= 500 && status < 600) {
            return "HTTP " + status + ": Server error.";
        }

        return "Unable to obtain a valid HTTP response. Status: "
                + status;
    }

    private String describeFailure(Exception exception) {

        if (exception instanceof TimeoutError) {
            return "Navigation timeout: Website did not respond "
                    + "within the allowed time.";
        }

        if (exception instanceof PlaywrightException) {
            return "Browser automation error: "
                    + exception.getMessage();
        }

        return "Execution error: " + exception.getMessage();
    }
}
