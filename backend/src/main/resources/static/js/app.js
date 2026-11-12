
document.addEventListener("DOMContentLoaded", () => {

    let currentWebsiteUrl = "";
    let isPlanApproved = false;
    let executions = [];

    // -------------------------------
    // 1. Navigation
    // -------------------------------

    const pageTitles = {
        dashboard: "Dashboard",
        "new-test": "Create Test Plan",
        executions: "Test Executions",
        bugs: "Bug Reports"
    };

    function navigateTo(page) {

        document.querySelectorAll(".page-section").forEach(section => {
            section.classList.add("d-none");
        });

        const selectedPage = document.getElementById(`${page}-page`);

        if (!selectedPage) return;

        selectedPage.classList.remove("d-none");

        document.querySelectorAll(".sidebar-nav .nav-link").forEach(link => {
            link.classList.toggle("active", link.dataset.page === page);
        });

        document.getElementById("page-title").textContent =
            pageTitles[page] || "DevLens AI";

        if (page === "executions") {
            loadExecutions();
        }
    }

    document.querySelectorAll("[data-page]").forEach(link => {
        link.addEventListener("click", event => {
            event.preventDefault();
            navigateTo(link.dataset.page);
        });
    });

    document.querySelectorAll("[data-navigate]").forEach(button => {
        button.addEventListener("click", () => {
            navigateTo(button.dataset.navigate);
        });
    });

    document.getElementById("create-test-btn").addEventListener("click", () => {
        navigateTo("new-test");
    });


    // -------------------------------
    // 2. Generate Test Plan
    // -------------------------------

    const testPlanForm = document.getElementById("test-plan-form");
    const generateButton = document.getElementById("generate-plan-btn");
    const resultSection = document.getElementById("test-plan-result");
    const stepsContainer = document.getElementById("test-plan-steps");

    testPlanForm.addEventListener("submit", async event => {

        event.preventDefault();

        const websiteUrl =
            document.getElementById("website-url").value.trim();

        const instruction =
            document.getElementById("test-instruction").value.trim();

        try {
            generateButton.disabled = true;
            generateButton.textContent = "Generating...";

            resultSection.classList.add("d-none");

            const response = await DevLensAPI.generateTestPlan(
                websiteUrl,
                instruction
            );

            if (!response.success || !response.data) {
                throw new Error(response.message || "Unable to generate test plan.");
            }

            currentWebsiteUrl = websiteUrl;
            isPlanApproved = false;

            renderTestPlan(response.data);

            resultSection.classList.remove("d-none");

        } catch (error) {
            alert(error.message || "Something went wrong.");

        } finally {
            generateButton.disabled = false;
            generateButton.textContent = "Generate Test Plan";
        }
    });


    // -------------------------------
    // 3. Render Test Plan
    // -------------------------------

    function renderTestPlan(plan) {

        stepsContainer.replaceChildren();

        (plan.testSteps || []).forEach((step, index) => {

            const wrapper = document.createElement("div");
            wrapper.className = "test-step";

            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.checked = true;
            checkbox.className = "step-checkbox";

            const label = document.createElement("label");
            label.textContent = `${index + 1}. ${step}`;

            wrapper.appendChild(checkbox);
            wrapper.appendChild(label);

            stepsContainer.appendChild(wrapper);
        });

        document.getElementById("approve-plan-btn").disabled = false;
        document.getElementById("execute-plan-btn").disabled = true;
        document.querySelector(".plan-status").textContent = "DRAFT";
    }


    // -------------------------------
    // 4. Approve Test Plan
    // -------------------------------

    document.getElementById("approve-plan-btn").addEventListener("click", () => {

        if (getSelectedSteps().length === 0) {
            alert("Please select at least one test step.");
            return;
        }

        isPlanApproved = true;

        document.getElementById("execute-plan-btn").disabled = false;
        document.querySelector(".plan-status").textContent = "APPROVED";

        alert("Test plan approved successfully!");
    });


    function getSelectedSteps() {

        const selectedSteps = [];

        document.querySelectorAll(".test-step").forEach(wrapper => {

            const checkbox = wrapper.querySelector(".step-checkbox");

            if (checkbox.checked) {
                selectedSteps.push(
                    wrapper.querySelector("label").textContent.replace(/^\d+\.\s*/, "")
                );
            }
        });

        return selectedSteps;
    }


    // -------------------------------
    // 5. Create Execution
    // -------------------------------

    document.getElementById("execute-plan-btn").addEventListener("click", async () => {

        if (!isPlanApproved) {
            alert("Please approve the test plan first.");
            return;
        }

        const executeButton = document.getElementById("execute-plan-btn");

        try {
            executeButton.disabled = true;
            executeButton.textContent = "Creating...";

            const response = await DevLensAPI.createExecution(
                currentWebsiteUrl,
                getSelectedSteps()
            );

            if (!response.success || !response.data) {
                throw new Error(response.message || "Execution request failed.");
            }

            await loadExecutions();

            alert("Execution request created successfully!");

            navigateTo("executions");

        } catch (error) {
            alert(error.message || "Unable to create execution.");

        } finally {
            executeButton.disabled = false;
            executeButton.textContent = "Execute Tests";
        }
    });


    // -------------------------------
    // 6. Load Executions From Backend
    // -------------------------------

    async function loadExecutions() {

        const executionList = document.getElementById("execution-list");

        executionList.textContent = "Loading execution history...";

        try {

            const response = await DevLensAPI.getAllExecutions();

            if (!response.success) {
                throw new Error(response.message || "Unable to load history.");
            }

            executions = response.data || [];

            renderExecutions();
            updateDashboard();

        } catch (error) {

            executionList.textContent =
                `Could not load execution history: ${error.message}`;

        }
    }


    // -------------------------------
    // 7. Render Execution History
    // -------------------------------

    function renderExecutions() {

        const executionList = document.getElementById("execution-list");
        const recentExecutions = document.getElementById("recent-executions");

        executionList.replaceChildren();
        recentExecutions.replaceChildren();

        if (executions.length === 0) {

            executionList.textContent = "No execution requests available.";

            const message = document.createElement("p");
            message.textContent = "Create your first AI test plan to get started.";

            recentExecutions.appendChild(message);

            return;
        }

        executions.forEach(execution => {
            executionList.appendChild(createExecutionElement(execution));
        });

        executions.slice(0, 5).forEach(execution => {
            recentExecutions.appendChild(createExecutionElement(execution));
        });
    }


    function createExecutionElement(execution) {

        const item = document.createElement("div");
        item.className = "execution-item";

        const details = document.createElement("div");

        const title = document.createElement("strong");
        title.textContent = execution.websiteUrl;

        const info = document.createElement("small");
        info.textContent = `Execution ID: ${execution.executionId}`;

        details.appendChild(title);
        details.appendChild(document.createElement("br"));
        details.appendChild(info);

        const status = document.createElement("span");
        status.className = "execution-status";
        status.textContent = execution.status;

        item.appendChild(details);
        item.appendChild(status);

        return item;
    }


    // -------------------------------
    // 8. Dashboard Statistics
    // -------------------------------

    function updateDashboard() {

        document.getElementById("total-runs").textContent =
            executions.length;

        const passed = executions.reduce(
            (total, item) => total + (item.passedTests || 0), 0
        );

        const failed = executions.reduce(
            (total, item) => total + (item.failedTests || 0), 0
        );

        document.getElementById("passed-tests").textContent = passed;
        document.getElementById("failed-tests").textContent = failed;
        document.getElementById("total-bugs").textContent = "0";
    }


    // -------------------------------
    // 9. Initial Dashboard Load
    // -------------------------------

    updateDashboard();
    navigateTo("dashboard");
    loadExecutions();

});
