package org.example.sample9.async;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class AsyncReportController {

    private final ReportService reportService;
    // A thread-safe map to act as our task database
    private final Map<String, Task> taskRegistry = new ConcurrentHashMap<>();

    public AsyncReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Scenario 8, Step 1: Client submits a request to start a long-running task.
     */
    @PostMapping("/reports/sales")
    public ResponseEntity<ReportSubmissionResponse> submitReportRequest(@RequestBody ReportRequest request) {
        // 1. Generate a unique ID for this task
        String taskId = UUID.randomUUID().toString();

        // 2. Create and register the task with PENDING status
        Task task = new Task(taskId, TaskStatus.PENDING, null);
        taskRegistry.put(taskId, task);

        // 3. Start the async process. This call returns immediately.
        reportService.generateReport(taskRegistry, taskId, request);

        // 4. Build the status URL for the client to poll
        String statusUrl = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/../queue/status/{taskId}") // Go up one level and then to the status path
            .buildAndExpand(taskId)
            .toUriString();

        // 5. Return 202 Accepted with the status URL
        ReportSubmissionResponse responseBody = new ReportSubmissionResponse("Report generation started", statusUrl);
        return ResponseEntity.accepted().body(responseBody);
    }

    /**
     * Scenario 8, Step 2: Client polls the status endpoint.
     */
    @GetMapping("/queue/status/{taskId}")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskId) {
        Task task = taskRegistry.get(taskId);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        if (task.status() == TaskStatus.COMPLETED) {
            // Task is done. Redirect the client to the final report URL.
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(task.resultUrl()))
                .build();
        } else {
            // Task is still pending or in progress. Return the current status.
            return ResponseEntity.ok(task);
        }
    }

    /**
     * Scenario 8, Step 3: Client downloads the final report.
     */
    @GetMapping("/reports/download/{reportFileId}")
    public ResponseEntity<String> downloadReport(@PathVariable String reportFileId) {
        // In a real app, you would fetch the report from storage and stream it.
        String reportContent = "This is the content of your sales report with ID: " + reportFileId;
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"report-" + reportFileId + ".txt\"")
            .body(reportContent);
    }
}
