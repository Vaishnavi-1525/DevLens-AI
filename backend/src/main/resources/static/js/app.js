
document.addEventListener("DOMContentLoaded", () => {

    // ==========================================
    // 1. DOM ELEMENTS
    // ==========================================

    const pageTitle = document.getElementById("page-title");

    const pages = {
        dashboard: document.getElementById("dashboard-page"),
        "new-test": document.getElementById("new-test-page"),
        executions: document.getElementById("executions-page"),
        bugs: document.getElementById("bugs-page")
    };

    const navLinks = document.querySelectorAll("[data-page]");
    const navigateButtons = document.querySelectorAll("[data-navigate]");

    const testPlanForm = document.getElementById("test-plan-form");
    const generatePlanBtn = document.getElementById("generate-plan-btn");
    const testPlanResult = document.getElementById("test-plan-result");
    const testPlanSteps = document.getElementById("test-plan-steps");
    const approvePlanBtn = document.getElementById("approve-plan-btn");
    const executePlanBtn = document.getElementById("execute-plan-btn");

    const executionList = document.getElementById("execution-list");
    const recentExecutions = document.getElementById("recent-executions");
    const refreshExecutionsBtn = document.getElementById("refresh-executions-btn");

    const executionDetails = document.getElementById("execution-details");
    const detailStatus = document.getElementById("detail-status");
    const detailWebsite = document.getElementById("detail-website");
    const detailId = document.getElementById("detail-id");
    const detailSummary = document.getElementById("detail-summary");
    const detailStarted = document.getElementById("detail-started");
    const detailCompleted = document.getElementById("detail-completed");
    const detailSteps = document.getElementById("detail-steps");

    // ==========================================
    // 2. APPLICATION STATE
    // ==========================================

    let currentTestPlan = null;
    let approvedSteps = [];
    let executions = [];

    // ==========================================
    // 3. HELPER FUNCTIONS
    // ==========================================

    function getResponseData(response) {
        return response && response.data !== undefined
            ? response.data
            : response;
    }

    function showMessage(message, type = "info") {
        const alert = document.createElement("div");

        alert.className = `alert alert-${type} mt-3`;
        alert.textContent = message;

        const activePage = document.querySelector(".page-section:not(.d-none)");

        if (activePage) {
            activePage.prepend(alert);

            setTimeout(() => {
                alert.remove();
            }, 4000);
        }
    }

    function formatDate(dateValue) {
        if (!dateValue) {
            return "Not available";
        }

        const date = new Date(dateValue);

        if (Number.isNaN(date.getTime())) {
            return dateValue;
        }

        return date.toLocaleString();
    }

    function createStatusBadge(status) {
        const badge = document.createElement("span");

        badge.className = "badge rounded-pill";

        const normalizedStatus = (status || "UNKNOWN").toUpperCase();

        const statusClasses = {
            PENDING: "text-bg-warning",
            RUNNING: "text-bg-info",
            PASSED: "text-bg-success",
            FAILED: "text-bg-danger",
            CANCELLED: "text-bg-secondary",
            DRAFT: "text-bg-secondary"
        };

        badge.classList.add(
            statusClasses[normalizedStatus] || "text-bg-secondary"
        );

        badge.textContent = normalizedStatus;

        return badge;
    }

    // ==========================================
    // 4. PAGE NAVIGATION
    // ==========================================

    function navigateTo(pageName) {

        if (!pages[pageName]) {
            return;
        }

        Object.values(pages).forEach(page => {
            page.classList.add("d-none");
        });

        pages[pageName].classList.remove("d-none");

        navLinks.forEach(link => {
            link.classList.toggle(
                "active",
                link.dataset.page === pageName
            );
        });

        const titles = {
            dashboard: "Dashboard",
            "new-test": "Create Test Plan",
            executions: "Test Executions",
            bugs: "Bug Reports"
        };

        pageTitle.textContent = titles[pageName];

        if (pageName === "executions") {
            loadExecutions();
        }
    }

    navLinks.forEach(link => {
        link.addEventListener("click", event => {
            event.preventDefault();
            navigateTo(link.dataset.page);
        });
    });

    navigateButtons.forEach(button => {
        button.addEventListener("click", () => {
            navigateTo(button.dataset.navigate);
        });
    });

    document.getElementById("create-test-btn")
        ?.addEventListener("click", () => {
            navigateTo("new-test");
        });

    // ==========================================
    // 5. GENERATE TEST PLAN
    // ==========================================

    testPlanForm.addEventListener("submit", async event => {

        event.preventDefault();

        const websiteUrl = document.getElementById("website-url").value.trim();
        const instruction = document.getElementById("test-instruction").value.trim();

        if (!websiteUrl || !instruction) {
            showMessage("Please enter website URL and testing instructions.", "warning");
            return;
        }

        generatePlanBtn.disabled = true;
        generatePlanBtn.textContent = "Generating...";

        testPlanResult.classList.add("d-none");
        currentTestPlan = null;
        approvedSteps = [];

        try {

            const response = await DevLensAPI.generateTestPlan(
                websiteUrl,
                instruction
            );

            const plan = getResponseData(response);

            if (!plan || !Array.isArray(plan.testSteps)) {
                throw new Error("Invalid test plan response from backend.");
            }

            currentTestPlan = plan;

            renderTestPlan(plan);

            testPlanResult.classList.remove("d-none");

            approvePlanBtn.disabled = true;
            executePlanBtn.disabled = true;

            showMessage("Test plan generated successfully. Review the steps.", "success");

        } catch (error) {

            console.error("Test plan error:", error);

            showMessage(
                error.message || "Unable to generate test plan.",
                "danger"
            );

        } finally {

            generatePlanBtn.disabled = false;
            generatePlanBtn.textContent = "Generate Test Plan";
        }
    });

    // ==========================================
    // 6. RENDER GENERATED TEST PLAN
    // ==========================================

    function renderTestPlan(plan) {

        testPlanSteps.replaceChildren();

        approvedSteps = [];

        plan.testSteps.forEach((step, index) => {

            const wrapper = document.createElement("div");
            wrapper.className = "form-check mb-3";

            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.className = "form-check-input";
            checkbox.id = `test-step-${index}`;
            checkbox.value = step;

            const label = document.createElement("label");
            label.className = "form-check-label";
            label.htmlFor = checkbox.id;
            label.textContent = step;

            checkbox.addEventListener("change", updateApprovalState);

            wrapper.appendChild(checkbox);
            wrapper.appendChild(label);

            testPlanSteps.appendChild(wrapper);
        });

        const status = testPlanResult.querySelector(".plan-status");

        if (status) {
            status.textContent = "DRAFT";
        }
    }

    // ==========================================
    // 7. APPROVE TEST PLAN
    // ==========================================

    function updateApprovalState() {

        const checkboxes = testPlanSteps.querySelectorAll(
            'input[type="checkbox"]:checked'
        );

        approvedSteps = Array.from(checkboxes).map(
            checkbox => checkbox.value
        );

        approvePlanBtn.disabled = approvedSteps.length === 0;

        if (approvedSteps.length === 0) {
            executePlanBtn.disabled = true;
        }
    }

    approvePlanBtn.addEventListener("click", () => {

        if (approvedSteps.length === 0) {
            showMessage("Select at least one test step.", "warning");
            return;
        }

        const status = testPlanResult.querySelector(".plan-status");

        if (status) {
            status.textContent = "APPROVED";
        }

        approvePlanBtn.disabled = true;
        executePlanBtn.disabled = false;

        showMessage(
            `${approvedSteps.length} test steps approved successfully.`,
            "success"
        );
    });

    // ==========================================
    // 8. CREATE TEST EXECUTION
    // ==========================================

    executePlanBtn.addEventListener("click", async () => {

        if (!currentTestPlan || approvedSteps.length === 0) {
            showMessage("Please generate and approve a test plan first.", "warning");
            return;
        }

        executePlanBtn.disabled = true;
        executePlanBtn.textContent = "Creating Execution...";

        try {

            const response = await DevLensAPI.createExecution(
                currentTestPlan.websiteUrl,
                approvedSteps
            );

            const execution = getResponseData(response);

            if (!execution || !execution.executionId) {
                throw new Error("Execution could not be created.");
            }

            showMessage(
                "Execution request created successfully.",
                "success"
            );

            await loadExecutions();

            navigateTo("executions");

            showExecutionDetails(execution);

        } catch (error) {

            console.error("Execution creation error:", error);

            showMessage(
                error.message || "Unable to create execution.",
                "danger"
            );

        } finally {

            executePlanBtn.disabled = false;
            executePlanBtn.textContent = "Execute Tests";
        }
    });

    // ==========================================
    // 9. LOAD EXECUTION HISTORY
    // ==========================================

    async function loadExecutions() {

        if (!executionList) {
            return;
        }

        executionList.replaceChildren();

        const loadingRow = document.createElement("tr");
        const loadingCell = document.createElement("td");

        loadingCell.colSpan = 7;
        loadingCell.className = "text-center";
        loadingCell.textContent = "Loading execution history...";

        loadingRow.appendChild(loadingCell);
        executionList.appendChild(loadingRow);

        try {

            const response = await DevLensAPI.getAllExecutions();

            const data = getResponseData(response);

            executions = Array.isArray(data) ? data : [];

            renderExecutions();
            updateDashboard();

        } catch (error) {

            console.error("Execution history error:", error);

            executionList.replaceChildren();

            const errorRow = document.createElement("tr");
            const errorCell = document.createElement("td");

            errorCell.colSpan = 7;
            errorCell.className = "text-center text-danger";
            errorCell.textContent = "Unable to load execution history.";

            errorRow.appendChild(errorCell);
            executionList.appendChild(errorRow);
        }
    }

    // ==========================================
    // 10. RENDER EXECUTION HISTORY
    // ==========================================

    function renderExecutions() {

        executionList.replaceChildren();
        recentExecutions.replaceChildren();

        if (executions.length === 0) {

            const emptyRow = document.createElement("tr");
            const emptyCell = document.createElement("td");

            emptyCell.colSpan = 7;
            emptyCell.className = "text-center text-muted";
            emptyCell.textContent = "No executions found.";

            emptyRow.appendChild(emptyCell);
            executionList.appendChild(emptyRow);

            recentExecutions.className = "empty-state";

            const message = document.createElement("p");
            message.textContent = "No executions yet. Create your first test plan.";

            recentExecutions.appendChild(message);

            return;
        }

        executions.forEach(execution => {
            executionList.appendChild(createExecutionRow(execution));
        });

        recentExecutions.className = "";

        executions.slice(0, 5).forEach(execution => {
            recentExecutions.appendChild(
                createRecentExecutionElement(execution)
            );
        });
    }

    // ==========================================
    // 11. CREATE EXECUTION TABLE ROW
    // ==========================================

    function createExecutionRow(execution) {

        const row = document.createElement("tr");

        const websiteCell = document.createElement("td");
        websiteCell.textContent = execution.websiteUrl || "Unknown";

        const idCell = document.createElement("td");
        idCell.textContent = execution.executionId
            ? execution.executionId.substring(0, 12) + "..."
            : "N/A";
        idCell.title = execution.executionId || "";

        const totalCell = document.createElement("td");
        totalCell.textContent = execution.totalTests ?? 0;

        const passedCell = document.createElement("td");
        passedCell.textContent = execution.passedTests ?? 0;

        const failedCell = document.createElement("td");
        failedCell.textContent = execution.failedTests ?? 0;

        const statusCell = document.createElement("td");
        statusCell.appendChild(createStatusBadge(execution.status));

        const actionCell = document.createElement("td");

        const viewButton = document.createElement("button");
        viewButton.className = "btn btn-sm btn-outline-primary";
        viewButton.textContent = "View Details";

        viewButton.addEventListener("click", () => {
            showExecutionDetails(execution);
        });

        actionCell.appendChild(viewButton);

        row.append(
            websiteCell,
            idCell,
            totalCell,
            passedCell,
            failedCell,
            statusCell,
            actionCell
        );

        return row;
    }

    // ==========================================
    // 12. RECENT EXECUTION CARD
    // ==========================================

    function createRecentExecutionElement(execution) {

        const card = document.createElement("div");
        card.className = "execution-item d-flex justify-content-between align-items-center border-bottom py-3";

        const info = document.createElement("div");

        const website = document.createElement("strong");
        website.textContent = execution.websiteUrl || "Unknown website";

        const id = document.createElement("small");
        id.className = "d-block text-muted";
        id.textContent = execution.executionId || "N/A";

        info.append(website, id);

        const status = createStatusBadge(execution.status);

        card.append(info, status);

        return card;
    }

    // ==========================================
    // 13. SHOW EXECUTION DETAILS
    // ==========================================

    function showExecutionDetails(execution) {

        if (!execution) {
            return;
        }

        executionDetails.classList.remove("d-none");

        detailStatus.replaceChildren();
        detailStatus.appendChild(
            createStatusBadge(execution.status)
        );

        detailWebsite.textContent = execution.websiteUrl || "N/A";
        detailId.textContent = execution.executionId || "N/A";
        detailSummary.textContent = execution.summary || "No summary available.";
        detailStarted.textContent = formatDate(execution.startedAt);
        detailCompleted.textContent = formatDate(execution.completedAt);

        // Display actual approved test steps returned by backend.
        detailSteps.replaceChildren();

        const steps = Array.isArray(execution.approvedSteps)
            ? execution.approvedSteps
            : [];

        if (steps.length === 0) {

            const emptyItem = document.createElement("li");
            emptyItem.className = "text-muted";
            emptyItem.textContent = "No approved test steps available.";

            detailSteps.appendChild(emptyItem);

        } else {

            steps.forEach((step, index) => {

                const listItem = document.createElement("li");
                listItem.className = "mb-2";
                listItem.textContent = step;

                detailSteps.appendChild(listItem);
            });
        }

        executionDetails.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });
    }

    // ==========================================
    // 14. UPDATE DASHBOARD COUNTS
    // ==========================================

    function updateDashboard() {

        const total = executions.length;

        const passed = executions.reduce(
            (sum, execution) => sum + (execution.passedTests || 0),
            0
        );

        const failed = executions.reduce(
            (sum, execution) => sum + (execution.failedTests || 0),
            0
        );

        const pending = executions.filter(
            execution => execution.status === "PENDING"
        ).length;

        document.getElementById("total-runs").textContent = total;
        document.getElementById("passed-tests").textContent = passed;
        document.getElementById("failed-tests").textContent = failed;

        document.getElementById("execution-total").textContent = total;
        document.getElementById("execution-passed").textContent = passed;
        document.getElementById("execution-failed").textContent = failed;
        document.getElementById("execution-pending").textContent = pending;
    }

    // ==========================================
    // 15. REFRESH EXECUTION HISTORY
    // ==========================================

    refreshExecutionsBtn?.addEventListener("click", () => {
        loadExecutions();
    });

    // ==========================================
    // 16. INITIAL DASHBOARD LOAD
    // ==========================================

    loadExecutions();

});
