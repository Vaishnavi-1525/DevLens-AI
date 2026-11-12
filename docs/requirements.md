

\# DevLens-AI

\## AI-Powered Website Testing and Bug Management Platform



\## 1. Project Overview



DevLens-AI is a web-based QA automation platform designed to simplify website testing.



It aims to convert natural-language testing instructions into structured test plans, execute approved scenarios through browser automation, identify failures, and generate useful bug reports.



The current implementation is an MVP foundation. AI-based test generation, database persistence, and advanced bug management are planned enhancements.



\## 2. Problem Statement



Manual website testing requires testers to create test cases, execute scenarios, identify failures, capture evidence, and prepare bug reports.



DevLens-AI aims to reduce repetitive QA work through AI-assisted test planning and automated browser execution.



\## 3. Project Objectives



\- Accept website URLs and natural-language testing instructions.

\- Generate structured test plans.

\- Allow testers to review and approve test plans.

\- Execute website checks using Playwright.

\- Display execution status and results.

\- Maintain execution history.

\- Identify failures and support bug reporting.

\- Provide AI-based analysis and developer insights in future versions.



\## 4. Functional Requirements



\### 4.1 Dashboard

\- Display total executions.

\- Display passed, failed, and pending test counts.

\- Show recent execution history.

\- Provide navigation between application modules.



\### 4.2 Test Plan Management

\- Accept a website URL.

\- Accept natural-language testing instructions.

\- Display generated test steps.

\- Allow users to select and approve test steps.



Current status: Basic workflow implemented with predefined sample test steps. Gemini integration is planned.



\### 4.3 Browser Automation

\- Launch Chromium using Playwright for Java.

\- Navigate to the provided website.

\- Check HTTP response status.

\- Verify page title.

\- Verify visible page content.

\- Record passed and failed checks.

\- Update execution status and timestamps.



Current status: Three basic browser checks implemented.



\### 4.4 Test Execution Management

\- Create execution requests.

\- Generate unique execution IDs.

\- Store approved test steps.

\- Display execution history.

\- Display execution details.

\- Track total, passed, and failed checks.



Current status: Implemented using in-memory storage. Data is lost when the application restarts.



\### 4.5 Bug Management

\- Generate structured bug reports.

\- Store reproduction steps and failure details.

\- Support severity and priority.

\- Track bug status.



Current status: Planned.



\### 4.6 AI Developer Insights

\- Analyze failed test results.

\- Suggest possible root causes.

\- Recommend potential fixes.



Current status: Planned.



\### 4.7 Jira Integration

\- Create Jira issues from approved bug reports.

\- Integrate using Jira REST APIs.

\- Synchronize issue status where supported.



Current status: Planned.



\## 5. Technology Stack



| Component | Technology |

|---|---|

| Programming Language | Java 21 |

| Backend Framework | Spring Boot 4.1.2 |

| Build Tool | Maven |

| Browser Automation | Playwright for Java 1.56.0 |

| Browser | Chromium |

| Frontend | HTML5, CSS3, Bootstrap 5 |

| Frontend Scripting | Vanilla JavaScript |

| Database (planned) | PostgreSQL |

| Database Client (planned) | DBeaver |

| AI Integration (planned) | Gemini API |

| Version Control | Git and GitHub |



\## 6. Backend Modules



\- controller

\- service

\- ai

\- automation

\- bug

\- entity

\- repository

\- config

\- dto



\## 7. Non-Functional Requirements



\- Modular and maintainable architecture.

\- Input validation and error handling.

\- Secure API key management.

\- Reliable browser execution.

\- Clear execution status and reporting.

\- Responsive and user-friendly dashboard.

\- Portable setup across Windows development machines.



\## 8. Security Requirements



\- Execute tests only on websites the user is authorized to test.

\- Validate submitted website URLs.

\- Protect API keys using environment variables.

\- Avoid logging passwords, tokens, or sensitive information.

\- Prevent unintended destructive browser actions.

\- Apply appropriate restrictions before exposing the application publicly.



\## 9. Current MVP Scope



1\. Spring Boot backend.

2\. REST API endpoints.

3\. Interactive dashboard.

4\. Test plan creation and approval workflow.

5\. Playwright Chromium integration.

6\. Basic website checks.

7\. Execution status and history.

8\. Execution details and approved-step display.



\## 10. Planned Enhancements



1\. Gemini-powered test plan generation.

2\. Execution of individual approved test steps.

3\. Individual test result display.

4\. Screenshot and execution log capture.

5\. PostgreSQL persistence.

6\. Bug report generation and management.

7\. AI-based root cause analysis.

8\. Developer fix suggestions.

9\. Jira integration.

10\. Advanced execution analytics and reporting.



\## 11. Project Constraints



\- The initial frontend uses HTML, CSS, Bootstrap 5, and vanilla JavaScript.

\- React is not part of the selected frontend stack.

\- The initial backend uses Java and Spring Boot.

\- The current execution records are stored in memory.

\- AI and database integrations will be introduced incrementally.



