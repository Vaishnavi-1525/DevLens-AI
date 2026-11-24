
package com.devlens.ai.bug;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bugs")
@CrossOrigin(origins = "*")
public class BugReportController {

    private static final long MAX_SCREENSHOT_SIZE =
            10 * 1024 * 1024;

    private static final byte[] PNG_SIGNATURE = {
            (byte) 0x89, 0x50, 0x4E, 0x47,
            0x0D, 0x0A, 0x1A, 0x0A
    };

    @Value("${devlens.screenshots.directory:${user.home}/.devlens-ai/screenshots}")
    private String screenshotDirectory;

    @Value("${devlens.traces.directory:${user.home}/.devlens-ai/traces}")
    private String traceDirectory;

    private final BugReportService bugReportService;

    public BugReportController(
            BugReportService bugReportService) {
        this.bugReportService = bugReportService;
    }

    @GetMapping
    public ResponseEntity<List<BugReport>> getAllReports() {

        return ResponseEntity.ok(
                bugReportService.getAllReports()
        );
    }

    @GetMapping("/{bugId}")
    public ResponseEntity<BugReport> getReport(
            @PathVariable String bugId) {

        BugReport report =
                bugReportService.getReport(bugId);

        if (report == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(report);
    }

    @PutMapping("/{bugId}/injected-phase")
    public ResponseEntity<?> updateInjectedPhase(
            @PathVariable String bugId,
            @RequestBody PhaseUpdateRequest request) {

        if (request == null
                || request.getInjectedPhase() == null
                || request.getInjectedPhase().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Injected phase is required.");
        }

        try {

            BugReport updatedReport =
                    bugReportService.updateInjectedPhase(
                            bugId,
                            request.getInjectedPhase()
                    );

            if (updatedReport == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(updatedReport);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    // Replace an existing bug screenshot.
    @PutMapping(
            value = "/{bugId}/screenshot",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> replaceScreenshot(
            @PathVariable String bugId,
            @RequestParam("file") MultipartFile file) {

        BugReport existingReport =
                bugReportService.getReport(bugId);

        if (existingReport == null) {
            return ResponseEntity.notFound().build();
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Please select a screenshot.");
        }

        if (file.getSize() > MAX_SCREENSHOT_SIZE) {
            return ResponseEntity.badRequest()
                    .body("Screenshot must not exceed 10 MB.");
        }

        try {

            byte[] imageBytes = file.getBytes();

            if (!isValidPng(imageBytes)) {
                return ResponseEntity.badRequest()
                        .body("Only valid PNG screenshots are supported.");
            }

            Path directory = Path.of(screenshotDirectory)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(directory);

            String fileName = bugId + "-"
                    + UUID.randomUUID()
                    + ".png";

            Path newScreenshot = directory
                    .resolve(fileName)
                    .normalize();

            if (!newScreenshot.startsWith(directory)) {
                return ResponseEntity.badRequest()
                        .body("Invalid screenshot path.");
            }

            Files.write(newScreenshot, imageBytes);

            String oldScreenshotPath =
                    existingReport.getScreenshotPath();

            BugReport updatedReport =
                    bugReportService.updateScreenshotPath(
                            bugId,
                            newScreenshot.toString()
                    );

            if (updatedReport == null) {

                Files.deleteIfExists(newScreenshot);

                return ResponseEntity.notFound().build();
            }

            deleteOldScreenshot(oldScreenshotPath, directory);

            return ResponseEntity.ok(updatedReport);

        } catch (IOException exception) {

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Unable to replace screenshot: "
                    + exception.getMessage());

        } catch (Exception exception) {

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Screenshot replacement failed.");
        }
    }

    // Validate PNG file signature.
    private boolean isValidPng(byte[] bytes) {

        if (bytes == null
                || bytes.length < PNG_SIGNATURE.length) {
            return false;
        }

        for (int i = 0; i < PNG_SIGNATURE.length; i++) {

            if (bytes[i] != PNG_SIGNATURE[i]) {
                return false;
            }
        }

        return true;
    }

    // Safely delete the previous screenshot.
    private void deleteOldScreenshot(
            String oldScreenshotPath,
            Path directory) {

        if (oldScreenshotPath == null
                || oldScreenshotPath.isBlank()) {
            return;
        }

        try {

            String oldFileName = Path.of(
                    oldScreenshotPath
            ).getFileName().toString();

            Path oldScreenshot = directory
                    .resolve(oldFileName)
                    .normalize();

            if (oldScreenshot.startsWith(directory)
                    && Files.isRegularFile(oldScreenshot)) {

                Files.deleteIfExists(oldScreenshot);
            }

        } catch (Exception ignored) {
            // Replacement is already successful.
        }
    }

    // Retrieve screenshot for a bug.
    @GetMapping("/{bugId}/screenshot")
    public ResponseEntity<Resource> getScreenshot(
            @PathVariable String bugId) {

        BugReport report =
                bugReportService.getReport(bugId);

        if (report == null
                || report.getScreenshotPath() == null
                || report.getScreenshotPath().isBlank()) {

            return ResponseEntity.notFound().build();
        }

        try {

            Path directory = Path.of(screenshotDirectory)
                    .toAbsolutePath()
                    .normalize();

            String fileName = Path.of(
                    report.getScreenshotPath()
            ).getFileName().toString();

            Path screenshot = directory
                    .resolve(fileName)
                    .normalize();

            if (!screenshot.startsWith(directory)
                    || !Files.isRegularFile(screenshot)) {

                return ResponseEntity.notFound().build();
            }

            Resource resource =
                    new FileSystemResource(screenshot);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);

        } catch (Exception exception) {

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    // NEW: Download Playwright Trace ZIP for a bug.
    @GetMapping("/{bugId}/trace")
    public ResponseEntity<Resource> getTrace(
            @PathVariable String bugId) {

        BugReport report =
                bugReportService.getReport(bugId);

        if (report == null
                || report.getTracePath() == null
                || report.getTracePath().isBlank()) {

            return ResponseEntity.notFound().build();
        }

        try {

            Path directory = Path.of(traceDirectory)
                    .toAbsolutePath()
                    .normalize();

            String fileName = Path.of(
                    report.getTracePath()
            ).getFileName().toString();

            Path traceFile = directory
                    .resolve(fileName)
                    .normalize();

            if (!traceFile.startsWith(directory)
                    || !Files.isRegularFile(traceFile)
                    || !fileName.toLowerCase().endsWith(".zip")) {

                return ResponseEntity.notFound().build();
            }

            Resource resource =
                    new FileSystemResource(traceFile);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            "application/zip"
                    ))
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\""
                    )
                    .body(resource);

        } catch (Exception exception) {

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    public static class PhaseUpdateRequest {

        private String injectedPhase;

        public String getInjectedPhase() {
            return injectedPhase;
        }

        public void setInjectedPhase(String injectedPhase) {
            this.injectedPhase = injectedPhase;
        }
    }
}
