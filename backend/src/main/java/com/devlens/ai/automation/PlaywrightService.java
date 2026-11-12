
package com.devlens.ai.automation;

import com.microsoft.playwright.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlaywrightService {

    public Map<String, Object> executeWebsiteChecks(String websiteUrl) {

        List<Map<String, String>> checks = new ArrayList<>();
        int passed = 0;
        int failed = 0;

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            try {
                Page page = browser.newPage();

                Response response = page.navigate(
                        websiteUrl,
                        new Page.NavigateOptions().setTimeout(30000)
                );

                int httpStatus = response != null ? response.status() : -1;

                boolean pageLoaded = httpStatus >= 200 && httpStatus < 400;

                addCheck(checks, "Website response",
                        pageLoaded,
                        "HTTP status: " + httpStatus);

                if (pageLoaded) {
                    passed++;
                } else {
                    failed++;
                }

                String title = page.title();
                boolean titlePresent = title != null && !title.isBlank();

                addCheck(checks, "Page title",
                        titlePresent,
                        titlePresent ? title : "Page title is empty");

                if (titlePresent) {
                    passed++;
                } else {
                    failed++;
                }

                String bodyText = page.locator("body").innerText();
                boolean contentPresent =
                        page.locator("body").isVisible()
                        && bodyText != null
                        && !bodyText.isBlank();

                addCheck(checks, "Page content",
                        contentPresent,
                        contentPresent
                                ? "Visible page content found"
                                : "Page content is empty or not visible");

                if (contentPresent) {
                    passed++;
                } else {
                    failed++;
                }

            } finally {
                browser.close();
            }

        } catch (Exception exception) {

            addCheck(checks, "Browser execution",
                    false, exception.getMessage());

            failed++;
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalTests", 3);
        result.put("passedTests", passed);
        result.put("failedTests", failed);
        result.put("checks", checks);

        return result;
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
}
