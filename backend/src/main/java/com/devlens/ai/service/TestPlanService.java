package com.devlens.ai.service;

import com.devlens.ai.dto.TestAction;
import com.devlens.ai.dto.TestCase;
import com.devlens.ai.dto.TestPlanResponse;
import com.devlens.ai.dto.TestRequest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class TestPlanService {

    public TestPlanResponse generatePlan(TestRequest request) {

        String instruction = request.getInstruction()
                .toLowerCase(Locale.ROOT);

        List<TestCase> testCases = new ArrayList<>();

        if (containsAny(instruction, "homepage", "home page",
                "website loads", "website loading", "open the website")) {
            testCases.add(createHomePageTest());
        }

        if (containsAny(instruction, "login", "log in",
                "authentication", "invalid username", "invalid password")) {
            testCases.add(createLoginTest());
        }

        if (containsAny(instruction, "checkbox", "check box")) {
            testCases.add(createCheckboxTest());
        }

        if (containsAny(instruction, "dropdown", "drop-down",
                "select option")) {
            testCases.add(createDropdownTest());
        }

        if (containsAny(instruction, "javascript alert",
                "javascript alerts", "js alert", "alert")) {
            testCases.add(createAlertTest());
        }

        if (containsAny(instruction, "dynamic loading",
                "loading process", "hello world")) {
            testCases.add(createDynamicLoadingTest());
        }

        if (testCases.isEmpty()) {
            testCases.add(createHomePageTest());
        }

        return new TestPlanResponse(
                request.getWebsiteUrl(),
                request.getInstruction(),
                "DRAFT",
                testCases
        );
    }

    private TestCase createHomePageTest() {
        return new TestCase(
                "TC-001",
                "Homepage Verification",
                "Verify that the website homepage loads successfully.",
                List.of(
                        action("NAVIGATE", null, "/", null, 30000),
                        action("ASSERT_TITLE", null, null, "The Internet", 10000),
                        action("ASSERT_VISIBLE", "body", null, null, 10000)
                ),
                "Homepage loads and displays the expected title and content."
        );
    }

    private TestCase createLoginTest() {
        return new TestCase(
                "TC-002",
                "Invalid Login Verification",
                "Verify that invalid login credentials display an error.",
                List.of(
                        action("NAVIGATE", null, "/login", null, 30000),
                        action("FILL", "#username", "tomsmith", null, 10000),
                        action("FILL", "#password", "invalid-password", null, 10000),
                        action("CLICK", "button[type='submit']", null, null, 10000),
                        action("ASSERT_TEXT_CONTAINS", "#flash", null,
                                "Your password is invalid!", 10000)
                ),
                "An appropriate invalid-password error message is displayed."
        );
    }

    private TestCase createCheckboxTest() {
        return new TestCase(
                "TC-003",
                "Checkbox Verification",
                "Verify checkbox selection and deselection.",
                List.of(
                        action("NAVIGATE", null, "/checkboxes", null, 30000),
                        action("SET_CHECKED", "input[type='checkbox']:nth-of-type(1)",
                                "true", null, 10000),
                        action("ASSERT_CHECKED", "input[type='checkbox']:nth-of-type(1)",
                                "true", null, 10000),
                        action("SET_CHECKED", "input[type='checkbox']:nth-of-type(2)",
                                "false", null, 10000),
                        action("ASSERT_CHECKED", "input[type='checkbox']:nth-of-type(2)",
                                "false", null, 10000)
                ),
                "First checkbox is selected and second checkbox is unchecked."
        );
    }

    private TestCase createDropdownTest() {
        return new TestCase(
                "TC-004",
                "Dropdown Verification",
                "Verify that Option 2 can be selected.",
                List.of(
                        action("NAVIGATE", null, "/dropdown", null, 30000),
                        action("SELECT_OPTION", "#dropdown", "2", null, 10000),
                        action("ASSERT_SELECTED_OPTION", "#dropdown", "2", null, 10000)
                ),
                "Option 2 is selected successfully."
        );
    }

    private TestCase createAlertTest() {
        return new TestCase(
                "TC-005",
                "JavaScript Alert Verification",
                "Verify that the JavaScript alert can be accepted.",
                List.of(
                        action("NAVIGATE", null, "/javascript_alerts", null, 30000),
                        action("ACCEPT_DIALOG_CLICK",
                                "button[onclick='jsAlert()']", null, null, 10000),
                        action("ASSERT_TEXT_CONTAINS", "#result", null,
                                "You successfully clicked an alert", 10000)
                ),
                "JavaScript alert is accepted and the success message appears."
        );
    }

    private TestCase createDynamicLoadingTest() {
        return new TestCase(
                "TC-006",
                "Dynamic Loading Verification",
                "Verify that dynamically loaded text becomes visible.",
                List.of(
                        action("NAVIGATE", null, "/dynamic_loading/1", null, 30000),
                        action("CLICK", "#start button", null, null, 10000),
                        action("WAIT_VISIBLE", "#finish h4", null, null, 15000),
                        action("ASSERT_TEXT_CONTAINS", "#finish h4", null,
                                "Hello World!", 10000)
                ),
                "Hello World text becomes visible after loading completes."
        );
    }

    private TestAction action(String type, String selector, String value,
                              String expectedText, int timeoutMs) {
        return new TestAction(type, selector, value, expectedText, timeoutMs);
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
