
const API_BASE_URL = "/api";

const DevLensAPI = {

    // Generate a test plan
    async generateTestPlan(websiteUrl, instruction) {

        const response = await fetch(`${API_BASE_URL}/tests/plan`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                websiteUrl,
                instruction
            })
        });

        if (!response.ok) {
            const error = await response.json().catch(() => null);

            throw new Error(
                error?.message || `Test plan request failed (${response.status})`
            );
        }

        return await response.json();
    },

    // Create an execution request
    async createExecution(websiteUrl, approvedSteps) {

        const response = await fetch(`${API_BASE_URL}/executions`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                websiteUrl,
                approvedSteps
            })
        });

        if (!response.ok) {
            throw new Error(`Execution request failed (${response.status})`);
        }

        return await response.json();
    },

    // Retrieve all executions
    async getAllExecutions() {

        const response = await fetch(`${API_BASE_URL}/executions`);

        if (!response.ok) {
            throw new Error(`Unable to retrieve executions (${response.status})`);
        }

        return await response.json();
    },

    // Retrieve one execution by ID
    async getExecution(executionId) {

        const response = await fetch(
            `${API_BASE_URL}/executions/${encodeURIComponent(executionId)}`
        );

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error("Execution not found.");
            }

            throw new Error(`Unable to retrieve execution (${response.status})`);
        }

        return await response.json();
    }

};
