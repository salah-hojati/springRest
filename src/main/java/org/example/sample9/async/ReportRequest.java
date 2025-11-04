package org.example.sample9.async;

import java.time.LocalDate;

// 1. An enum to represent the status of a task
enum TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}

// 2. A record to hold the state of a single task
record Task(String taskId, TaskStatus status, String resultUrl) {
    // Helper methods to create new instances with updated status
    public Task withStatus(TaskStatus newStatus) {
        return new Task(this.taskId, newStatus, this.resultUrl);
    }
    public Task withResultUrl(String newResultUrl) {
        return new Task(this.taskId, this.status, newResultUrl);
    }
}

// 3. A DTO for the initial request body
public record ReportRequest(LocalDate fromDate, LocalDate toDate) {}

// 4. A DTO for the initial response body
record ReportSubmissionResponse(String message, String statusUrl) {}
