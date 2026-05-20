package com.pid.backend.controller;

import com.pid.backend.service.CsvShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/csv")
@RequiredArgsConstructor
public class CsvController {

    private final CsvShowService csvShowService;

    @PostMapping("/shows/import")
    public Map<String, Object> importShows(@RequestParam("file") MultipartFile file) {
        int importedCount = csvShowService.importShows(file);

        return Map.of(
                "message", "Shows imported successfully",
                "importedCount", importedCount
        );
    }

    @GetMapping("/shows/export")
    public ResponseEntity<byte[]> exportShows() {
        byte[] csvBytes = csvShowService.exportShows().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=shows.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }
}