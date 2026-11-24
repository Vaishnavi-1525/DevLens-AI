package com.devlens.ai.dto;

public class TestAction {

    private String type;
    private String selector;
    private String value;
    private String expectedText;
    private int timeoutMs;

    public TestAction() {
    }

    public TestAction(String type, String selector, String value,
                      String expectedText, int timeoutMs) {
        this.type = type;
        this.selector = selector;
        this.value = value;
        this.expectedText = expectedText;
        this.timeoutMs = timeoutMs;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSelector() { return selector; }
    public void setSelector(String selector) { this.selector = selector; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getExpectedText() { return expectedText; }
    public void setExpectedText(String expectedText) {
        this.expectedText = expectedText;
    }

    public int getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
}
