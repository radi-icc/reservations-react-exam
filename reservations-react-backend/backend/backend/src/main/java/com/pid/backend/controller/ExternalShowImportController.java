package com.pid.backend.controller;

import com.pid.backend.service.ExternalShowImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/external-shows")
@RequiredArgsConstructor
public class ExternalShowImportController {

    private final ExternalShowImportService externalShowImportService;

    @PostMapping("/import")
    public Map<String, Object> importExternalShows(
            @RequestParam Long defaultLocationId
    ) {
        log.info("External show import request received. defaultLocationId={}", defaultLocationId);

        int importedCount = externalShowImportService.importShowsFromExternalApi(defaultLocationId);

        log.info("External show import completed successfully. importedCount={}", importedCount);

        return Map.of(
                "message", "External shows imported successfully",
                "importedCount", importedCount
        );
    }
}