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
@RequestMapping("/api")
public class AsyncReportController {

    private final ReportService reportService;
    private final Map<String, Task> taskRegistry = new ConcurrentHashMap<>();

    public AsyncReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/reports/sales")
    public ResponseEntity<ReportSubmissionResponse> submitReportRequest(@RequestBody ReportRequest request) {
        String taskId = UUID.randomUUID().toString();

        Task task = new Task(taskId, TaskStatus.PENDING, null);
        taskRegistry.put(taskId, task);

        // Get base URL from current request context (available here in controller)
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        // Pass baseUrl to the async service method
        reportService.generateReport(taskRegistry, taskId, request, baseUrl);

        // Build status URL
        String statusUrl = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/../queue/status/{taskId}")
                .buildAndExpand(taskId)
                .toUriString();

        ReportSubmissionResponse responseBody = new ReportSubmissionResponse("Report generation started", statusUrl);
        return ResponseEntity.accepted().body(responseBody);
    }

    @GetMapping("/queue/status/{taskId}")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskId) {
        Task task = taskRegistry.get(taskId);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        if (task.status() == TaskStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(URI.create(task.resultUrl()))
                    .build();
        } else {
            return ResponseEntity.ok(task);
        }
    }

    @GetMapping("/reports/download/{reportFileId}")
    public ResponseEntity<String> downloadReport(@PathVariable String reportFileId) {
        String reportContent = "This is the content of your sales report with ID: " + reportFileId;
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"report-" + reportFileId + ".txt\"")
                .body(reportContent);
    }
}