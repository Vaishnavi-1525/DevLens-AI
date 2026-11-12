

\# DevLens-AI – System Architecture



\## 1. Architecture Overview



DevLens-AI follows a modular client-server architecture.



The frontend is served by the Spring Boot application and communicates with backend REST APIs using JavaScript Fetch API.



The backend manages test planning, execution requests, browser automation, and execution results.



\## 2. Technology Architecture



\### Frontend

\- HTML5

\- CSS3

\- Bootstrap 5

\- Vanilla JavaScript



\### Backend

\- Java 21

\- Spring Boot 4.1.2

\- Maven

\- Spring Web

\- Spring Validation



\### Browser Automation

\- Playwright for Java 1.56.0

\- Chromium Headless Browser



\### Planned Components

\- Gemini API for AI test planning

\- PostgreSQL for persistent storage

\- DBeaver for database management

\- Jira REST API for defect integration



\## 3. Application Architecture



```text

User

&#x20;|

&#x20;v

Web Dashboard

(HTML + CSS + Bootstrap + JavaScript)

&#x20;|

&#x20;| HTTP REST API

&#x20;v

Spring Boot Application

&#x20;|

&#x20;+---- Controller Layer

&#x20;|       |

&#x20;|       +---- HealthController

&#x20;|       +---- ProjectController

&#x20;|       +---- TestPlanController

&#x20;|       +---- TestExecutionController

&#x20;|

&#x20;+---- Service Layer

&#x20;|       |

&#x20;|       +---- TestPlanService

&#x20;|       +---- TestExecutionService

&#x20;|

&#x20;+---- Automation Layer

&#x20;|       |

&#x20;|       +---- PlaywrightService

&#x20;|       +---- TestExecutionResult

&#x20;|       +---- TestExecutionStatus

&#x20;|

&#x20;+---- DTO Layer

&#x20;|

&#x20;+---- Configuration Layer

&#x20;|

&#x20;v

Chromium Browser

&#x20;|

&#x20;v

Target Website

```



\## 4. Application Workflow



1\. User opens the DevLens-AI dashboard.

2\. User enters a website URL and testing instructions.

3\. Frontend sends a request to the Spring Boot backend.

4\. TestPlanService currently returns predefined sample test steps.

5\. User reviews and approves the test steps.

6\. Frontend submits the approved steps to the execution API.

7\. TestExecutionService creates an execution record.

8\. PlaywrightService launches Chromium.

9\. Chromium navigates to the target website.

10\. Basic checks are performed:

&#x20;   - HTTP response status

&#x20;   - Page title

&#x20;   - Visible page content

11\. Execution results and timestamps are updated.

12\. Dashboard displays execution history and details.



Note: Approved steps are currently stored but are not individually interpreted or executed.



\## 5. Backend Package Structure



```text

com.devlens.ai

|

+-- controller

+-- service

+-- ai

+-- automation

+-- bug

+-- entity

+-- repository

+-- config

+-- dto

```



\## 6. Current REST API Design



| Method | Endpoint | Purpose |

|---|---|---|

| GET | /api/health | Backend health check |

| GET | /api/project/info | Project information |

| POST | /api/tests/plan | Generate a test plan |

| POST | /api/executions | Create and execute browser checks |

| GET | /api/executions | Retrieve execution history |

| GET | /api/executions/{executionId} | Retrieve execution details |



\## 7. Execution Data Flow



The current implementation uses ConcurrentHashMap for storing execution records.



Each execution contains:

\- Execution ID

\- Website URL

\- Approved test steps

\- Execution status

\- Total checks

\- Passed checks

\- Failed checks

\- Start timestamp

\- Completion timestamp

\- Execution summary



Current limitation: Execution records are stored in memory and are lost when the application restarts.



\## 8. Security Considerations



\- Validate website URLs before browser navigation.

\- Execute tests only on authorized websites.

\- Store API keys in environment variables.

\- Do not expose secrets in logs or source code.

\- Avoid destructive browser actions.

\- Introduce appropriate network restrictions before public deployment.



\## 9. Future Architecture Enhancements



\- Gemini-powered test planning.

\- Individual approved-step execution.

\- PostgreSQL persistence through Spring Data JPA.

\- Screenshot and log evidence management.

\- Bug detection and reporting.

\- AI-based root cause analysis.

\- Jira integration.

\- Improved execution isolation and security.
