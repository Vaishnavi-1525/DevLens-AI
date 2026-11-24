package com.devlens.ai.dto;

import java.util.List;

public class TestCase {

    private String id;
    private String name;
    private String description;
    private List<TestAction> actions;
    private String expectedResult;

    public TestCase() {
    }

    public TestCase(String id, String name, String description,
                    List<TestAction> actions, String expectedResult) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.actions = actions;
        this.expectedResult = expectedResult;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<TestAction> getActions() { return actions; }
    public void setActions(List<TestAction> actions) {
        this.actions = actions;
    }

    public String getExpectedResult() { return expectedResult; }
    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }
}
