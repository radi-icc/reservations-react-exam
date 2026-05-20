package com.pid.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pid.backend.entity.Location;
import com.pid.backend.entity.Show;
import com.pid.backend.exception.BadRequestException;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.LocationRepository;
import com.pid.backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalShowImportService {

    private final ShowRepository showRepository;
    private final LocationRepository locationRepository;
    private final ObjectMapper objectMapper;

    @Value("${external.shows.api-url}")
    private String externalShowsApiUrl;

    public int importShowsFromExternalApi(Long defaultLocationId) {
        log.info("Starting external show import. defaultLocationId={}, apiUrl={}",
                defaultLocationId,
                externalShowsApiUrl
        );

        Location defaultLocation = locationRepository.findById(defaultLocationId)
                .orElseThrow(() -> {
                    log.error("External import failed. Location not found. defaultLocationId={}", defaultLocationId);
                    return new ResourceNotFoundException("Location not found with id: " + defaultLocationId);
                });

        try {
            RestTemplate restTemplate = new RestTemplate();

            log.info("Calling external shows API: {}", externalShowsApiUrl);

            String response = restTemplate.getForObject(externalShowsApiUrl, String.class);

            if (response == null || response.isBlank()) {
                log.error("External API returned empty response. apiUrl={}", externalShowsApiUrl);
                throw new BadRequestException("External API returned empty response");
            }

            log.info("External API response received. responseLength={}", response.length());

            JsonNode root = objectMapper.readTree(response);
            JsonNode items = extractItems(root);

            if (items == null || !items.isArray()) {
                log.error("External API response format unsupported. rootFields={}", root.fieldNames());
                throw new BadRequestException("External API response format is not supported");
            }

            log.info("External API items found. itemCount={}", items.size());

            int importedCount = 0;
            int skippedCount = 0;

            for (JsonNode item : items) {
                String title = extractText(
                        item,
                        "title",
                        "name",
                        "nom",
                        "naam",
                        "designation",
                        "denomination",
                        "label",
                        "event_name",
                        "translations_fr_name",
                        "translations_nl_name",
                        "translations_en_name"
                );

                if (title == null || title.isBlank()) {
                    skippedCount++;
                    log.warn("Skipping external item because title was not found. item={}", item);
                    continue;
                }

                String description = extractText(
                        item,
                        "description",
                        "description_fr",
                        "description_en",
                        "summary",
                        "body",
                        "address",
                        "adresse",
                        "translations_fr_address_line1",
                        "translations_nl_address_line1",
                        "add_municipality_fr",
                        "add_municipality_nl",
                        "visit_category_fr_multi",
                        "visit_category_en_multi"
                );

                String posterUrl = extractText(
                        item,
                        "posterUrl",
                        "image",
                        "image_url",
                        "poster",
                        "photo",
                        "thumbnail",
                        "media"
                );

                Show show = Show.builder()
                        .title(title)
                        .slug(generateUniqueSlug(title, item))
                        .posterUrl(posterUrl)
                        .bookable(true)
                        .price(BigDecimal.ZERO)
                        .description(description)
                        .location(defaultLocation)
                        .createdAt(LocalDateTime.now())
                        .build();

                showRepository.save(show);
                importedCount++;

                log.info("Imported external show. title={}, locationId={}", title, defaultLocation.getId());
            }

            log.info("External show import finished. importedCount={}, skippedCount={}",
                    importedCount,
                    skippedCount
            );

            return importedCount;

        } catch (BadRequestException | ResourceNotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error("External show import failed. apiUrl={}, defaultLocationId={}",
                    externalShowsApiUrl,
                    defaultLocationId,
                    exception
            );

            throw new BadRequestException("Failed to import shows from external API: " + exception.getMessage());
        }
    }

    private JsonNode extractItems(JsonNode root) {
        if (root.isArray()) {
            return root;
        }

        if (root.has("results")) {
            return root.get("results");
        }

        if (root.has("records")) {
            return root.get("records");
        }

        if (root.has("data")) {
            return root.get("data");
        }

        return null;
    }

    private String extractText(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            if (node.has(fieldName) && !node.get(fieldName).isNull()) {
                return node.get(fieldName).asText();
            }
        }

        if (node.has("fields") && node.get("fields").isObject()) {
            JsonNode fieldsNode = node.get("fields");

            for (String fieldName : fieldNames) {
                if (fieldsNode.has(fieldName) && !fieldsNode.get(fieldName).isNull()) {
                    return fieldsNode.get(fieldName).asText();
                }
            }
        }

        return null;
    }

    private String generateSlug(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return normalized
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    private String generateUniqueSlug(String title, JsonNode item) {
        String baseSlug = generateSlug(title);

        String externalId = extractText(item, "id");

        if (externalId != null && !externalId.isBlank()) {
            return baseSlug + "-" + externalId;
        }

        return baseSlug + "-" + System.currentTimeMillis();
    }
}