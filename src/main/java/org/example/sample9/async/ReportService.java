package org.example.sample9.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    // This method will run in a background thread
    @Async
    public void generateReport(Map<String, Task> taskRegistry, String taskId, ReportRequest request) {
        logger.info("Starting report generation for task ID: {}", taskId);

        try {
            // 1. Set status to IN_PROGRESS
            taskRegistry.computeIfPresent(taskId, (id, task) -> task.withStatus(TaskStatus.IN_PROGRESS));

            // 2. Simulate a long-running process (e.g., 10 seconds)
            Thread.sleep(10000);

            // 3. Generate a unique ID for the final report file
            String reportFileId = UUID.randomUUID().toString();

            // 4. Build the final download URL for the completed report
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/reports/download/{reportFileId}")
                .buildAndExpand(reportFileId)
                .toUriString();

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
