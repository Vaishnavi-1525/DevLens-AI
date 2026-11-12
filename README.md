#

# \# DevLens-AI

#

# \*\*AI-Powered Website Testing and Bug Management Platform\*\*

#

# DevLens-AI is a web-based QA automation platform built using Java, Spring Boot, Playwright, and a Bootstrap-based frontend.

#

# It aims to simplify website testing through structured test planning, browser automation, execution tracking, and AI-assisted defect analysis.

#

# \## Current Features

#

# \- Interactive web dashboard.

# \- Website URL and testing instruction form.

# \- Test plan generation and approval workflow.

# \- Playwright Chromium browser automation.

# \- HTTP response status validation.

# \- Page title validation.

# \- Visible page content validation.

# \- Test execution history.

# \- Execution status and result tracking.

# \- Execution details with approved test steps.

#

# \*\*Current limitation:\*\* Test plans use predefined sample steps. Browser automation currently performs three fixed checks. Execution history is stored in memory.

#

# \## Technology Stack

#

# | Technology | Purpose |

# |---|---|

# | Java 21 | Backend programming |

# | Spring Boot 4.1.2 | Backend framework |

# | Maven | Build management |

# | Playwright Java 1.56.0 | Browser automation |

# | Chromium | Browser execution |

# | HTML, CSS, Bootstrap 5 | Frontend |

# | Vanilla JavaScript | Frontend interactions |

# | Git and GitHub | Version control |

# | PostgreSQL | Planned database |

# | Gemini API | Planned AI integration |

#

# \## Project Structure

#

# ```text

# DevLens-AI/

# |

# +-- backend/

# |   +-- src/

# |   |   +-- main/

# |   |   |   +-- java/com/devlens/ai/

# |   |   |   |   +-- controller/

# |   |   |   |   +-- service/

# |   |   |   |   +-- automation/

# |   |   |   |   +-- dto/

# |   |   |   |   +-- config/

# |   |   |   |   +-- ai/

# |   |   |   |   +-- bug/

# |   |   |   |   +-- entity/

# |   |   |   |   +-- repository/

# |   |   |   |

# |   |   |   +-- resources/

# |   |   |       +-- static/

# |   |   |           +-- css/

# |   |   |           +-- js/

# |   |   |

# |   |   +-- test/

# |   |

# |   +-- pom.xml

# |   +-- mvnw

# |   +-- mvnw.cmd

# |   +-- .gitignore

# |

# +-- docs/

# |   +-- requirements.md

# |   +-- architecture.md

# |

# +-- .gitignore

# +-- README.md

# ```

#

# \## Prerequisites

#

# Install the following on your Windows development machine:

#

# 1\. JDK 21 (Temurin or another compatible distribution).

# 2\. Git.

# 3\. Internet access for Maven dependency downloads and Playwright browser installation.

#

# Maven is included through the Maven Wrapper, so a separate Maven installation is not required.

#

# PostgreSQL and Gemini API credentials are not required for the current MVP.

#

# \## Setup on a New Windows Laptop

#

# \### 1. Clone the repository

#

# Open PowerShell:

#

# ```powershell

# git clone https://github.com/Vaishnavi-1525/DevLens-AI.git

# cd DevLens-AI

# ```

#

# \### 2. Verify Java

#

# ```powershell

# java -version

# ```

#

# Ensure Java 21 is installed.

#

# If Java is installed but not available in PATH, configure JAVA\_HOME and PATH for the current PowerShell session.

#

# Example:

#

# ```powershell

# $env:JAVA\_HOME = "$env:USERPROFILE\\AppData\\Local\\Programs\\Eclipse Adoptium\\jdk-21.0.13.9-hotspot"

# $env:Path = "$env:JAVA\_HOME\\bin;$env:Path"

# ```

#

# Adjust the path to match the JDK installation on your machine.

#

# \### 3. Build the backend

#

# ```powershell

# cd backend

# .\\mvnw.cmd clean package

# ```

#

# The first build downloads the required Maven dependencies.

#

# \### 4. Install the Playwright Chromium browser

#

# Run:

#

# ```powershell

# .\\mvnw.cmd "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install chromium" org.codehaus.mojo:exec-maven-plugin:3.7.0:java

# ```

#

# This downloads the Chromium browser required for automated testing.

#

# \### 5. Start the application

#

# ```powershell

# .\\mvnw.cmd spring-boot:run

# ```

#

# Wait until Spring Boot starts successfully.

#

# \### 6. Open the dashboard

#

# Open your browser:

#

# http://localhost:8080

#

# Backend health endpoint:

#

# http://localhost:8080/api/health

#

# \## Basic Usage

#

# 1\. Open the dashboard.

# 2\. Navigate to Create Test Plan.

# 3\. Enter a website URL.

# 4\. Enter testing instructions.

# 5\. Generate the test plan.

# 6\. Review and approve the displayed steps.

# 7\. Click Execute.

# 8\. View execution status and results under Test Executions.

#

# Example website:

#

# ```text

# https://example.com

# ```

#

# \## Current API Endpoints

#

# | Method | Endpoint |

# |---|---|

# | GET | /api/health |

# | GET | /api/project/info |

# | POST | /api/tests/plan |

# | POST | /api/executions |

# | GET | /api/executions |

# | GET | /api/executions/{executionId} |

#

# \## Important Notes

#

# \- Execution records currently use in-memory storage.

# \- Restarting the application clears execution history.

# \- AI-powered test generation is not yet integrated.

# \- Approved natural-language steps are not individually executed yet.

# \- PostgreSQL persistence is planned.

# \- Execute browser tests only against websites you are authorized to test.

# \- Do not commit API keys, credentials, or sensitive data.

#

# \## Development Roadmap

#

# \- \[x] Spring Boot backend foundation.

# \- \[x] REST API foundation.

# \- \[x] Interactive dashboard foundation.

# \- \[x] Test plan approval workflow.

# \- \[x] Playwright Chromium integration.

# \- \[x] Basic automated website checks.

# \- \[x] In-memory execution tracking.

# \- \[ ] Individual test-step result reporting.

# \- \[ ] Gemini AI integration.

# \- \[ ] PostgreSQL persistence.

# \- \[ ] Screenshot and log capture.

# \- \[ ] Bug report management.

# \- \[ ] AI root cause analysis.

# \- \[ ] Jira integration.

#

# \## Documentation

#

# \- \[Project Requirements](docs/requirements.md)

# \- \[System Architecture](docs/architecture.md)

#
