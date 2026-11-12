\# DevLens-AI – System Architecture



\## 1. Architecture Overview



DevLens-AI follows a modular client-server architecture.



The frontend communicates with the Spring Boot backend through REST APIs.



\## 2. Main Components



\### Frontend

\- HTML

\- CSS

\- Bootstrap 5

\- JavaScript



\### Backend

\- Java 21

\- Spring Boot

\- Maven

\- REST APIs



\### AI Test Planner

\- Gemini API

\- Natural-language instruction processing

\- Test scenario generation



\### Automation Engine

\- Playwright for Java

\- Browser interaction

\- Test execution

\- Screenshot and log capture



\### Database

\- PostgreSQL

\- DBeaver



\### External Integration

\- Jira REST API



\## 3. Application Workflow



1\. User enters a website URL and testing instruction.

2\. Frontend sends the request to Spring Boot.

3\. AI Test Planner converts the instruction into structured test steps.

4\. User reviews and approves the test plan.

5\. Playwright executes the approved steps.

6\. Execution results and screenshots are collected.

7\. Failed scenarios are analyzed.

8\. Bug reports are generated.

9\. Results are stored in PostgreSQL.

10\. Approved bugs can be sent to Jira.



\## 4. Backend Package Structure



\- controller

\- service

\- ai

\- automation

\- bug

\- entity

\- repository

\- config



\## 5. API Design



\- GET /api/health

\- POST /api/tests/plan

\- POST /api/tests/execute

\- GET /api/bugs

\- GET /api/tests/history



\## 6. Security



\- Validate user inputs.

\- Restrict browser execution to authorized websites.

\- Protect API keys using environment variables.

\- Prevent destructive browser actions.

\- Require approval before risky operations.



\## 7. Future Scope



\- AI-based root cause analysis.

\- Jira integration.

\- Advanced test analytics.

\- Developer fix recommendations.

