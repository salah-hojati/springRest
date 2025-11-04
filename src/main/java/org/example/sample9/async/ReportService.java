package org.example.sample9.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    // Accept baseUrl as parameter instead of trying to get it from request context
    @Async
    public void generateReport(Map<String, Task> taskRegistry, String taskId, ReportRequest request, String baseUrl) {
        logger.info("Starting report generation for task ID: {}", taskId);

        try {
            // 1. Set status to IN_PROGRESS
            taskRegistry.computeIfPresent(taskId, (id, task) -> task.withStatus(TaskStatus.IN_PROGRESS));

            // 2. Simulate a long-running process (e.g., 10 seconds)
            Thread.sleep(30000);

            // 3. Generate a unique ID for the final report file
            String reportFileId = UUID.randomUUID().toString();

            // 4. Build the final download URL using the provided baseUrl
            String downloadUrl = baseUrl + "/api/reports/download/" + reportFileId;

            // 5. Mark the task as COMPLETED and set the result URL
            taskRegistry.computeIfPresent(taskId, (id, task) ->
                    task.withStatus(TaskStatus.COMPLETED).withResultUrl(downloadUrl)
            );

            logger.info("Successfully completed report generation for task ID: {}", taskId);

        } catch (InterruptedException e) {
            logger.error("Report generation was interrupted for task ID: {}", taskId, e);
            taskRegistry.computeIfPresent(taskId, (id, task) -> task.withStatus(TaskStatus.FAILED));
            Thread.currentThread().interrupt();
        }
    }
}