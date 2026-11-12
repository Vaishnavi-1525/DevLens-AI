

\# DevLens-AI

\## AI-Powered Autonomous Website Testing Platform



\### 1. Project Overview

DevLens-AI is an AI-powered web testing platform that converts natural-language instructions into executable browser tests, identifies failures, and generates structured bug reports with supporting evidence.



\### 2. Problem Statement

Manual website testing requires significant time and effort. Testers must manually create test cases, execute them, capture evidence, and prepare bug reports.



DevLens-AI aims to automate these repetitive QA activities using AI and browser automation.



\### 3. Project Objectives

\- Convert natural-language instructions into test cases.

\- Execute test cases automatically using Playwright.

\- Detect unexpected behaviour and test failures.

\- Capture screenshots and execution logs.

\- Generate structured bug reports.

\- Provide AI-based root cause hints and fix suggestions.

\- Manage bugs and test execution history.

\- Support Jira integration.



\### 4. Core Features



\#### 4.1 AI Test Planner

\- Accept natural-language testing instructions.

\- Generate structured test scenarios and steps.

\- Identify test actions and expected outcomes.

\- Allow users to review test plans before execution.



\#### 4.2 Browser Automation

\- Execute approved test steps using Playwright.

\- Support navigation, clicks, text input and assertions.

\- Capture screenshots and execution logs.

\- Handle execution failures safely.



\#### 4.3 Bug Detection and Reporting

\- Detect failed test steps.

\- Generate bug titles and descriptions.

\- Include reproduction steps.

\- Assign severity and priority.

\- Attach screenshots and logs.



\#### 4.4 Bug Management

\- View and filter reported bugs.

\- Track bug status.

\- Maintain execution history.



\#### 4.5 AI Developer Insights

\- Analyze failure information.

\- Suggest possible root causes.

\- Recommend potential fixes.



\#### 4.6 Jira Integration

\- Connect with Jira using REST APIs.

\- Create Jira issues from approved bug reports.

\- Sync issue status where supported.



\### 5. Technology Stack



\- Backend: Java 21, Spring Boot

\- Build Tool: Maven

\- Browser Automation: Playwright for Java

\- AI: Gemini API

\- Database: PostgreSQL

\- Database Client: DBeaver

\- Frontend: HTML, CSS, Bootstrap 5, JavaScript

\- Version Control: Git and GitHub



\### 6. Main Backend Modules



\- Controller

\- Service

\- AI Test Planner

\- Automation Engine

\- Bug Management

\- Entity

\- Repository

\- Configuration



\### 7. Non-Functional Requirements

\- Secure API key management.

\- Input validation and safe browser execution.

\- Reliable test execution and evidence storage.

\- Maintainable modular architecture.

\- Clear error handling and logging.



\### 8. Safety Requirements

\- Execute tests only on authorized websites.

\- Never perform real payments or destructive actions.

\- Require approval for risky browser actions.

\- Never expose credentials or API keys.



\### 9. MVP Scope

The initial version will support:

1\. Natural-language test instructions.

2\. AI-generated test plans.

3\. Playwright browser execution.

4\. Screenshot capture.

5\. Automatic bug report generation.

6\. Basic bug dashboard.



\### 10. Future Enhancements

\- Jira integration.

\- AI root cause analysis.

\- Developer fix recommendations.

\- Advanced test analytics.

\- Test execution history and reporting.



