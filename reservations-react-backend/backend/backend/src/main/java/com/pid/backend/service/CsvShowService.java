package com.pid.backend.service;

import com.pid.backend.entity.Location;
import com.pid.backend.entity.Show;
import com.pid.backend.exception.BadRequestException;
import com.pid.backend.exception.ResourceNotFoundException;
import com.pid.backend.repository.LocationRepository;
import com.pid.backend.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CsvShowService {

    private final ShowRepository showRepository;
    private final LocationRepository locationRepository;

    public int importShows(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("CSV file is required");
        }

        int importedCount = 0;

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
                )
        ) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                String[] values = line.split(",", -1);

                if (values.length < 6) {
                    throw new BadRequestException("Invalid CSV format. Expected columns: title,posterUrl,bookable,price,description,locationId");
                }

                String title = values[0].trim();
                String posterUrl = values[1].trim();
                Boolean bookable = Boolean.parseBoolean(values[2].trim());
                BigDecimal price = new BigDecimal(values[3].trim());
                String description = values[4].trim();
                Long locationId = Long.parseLong(values[5].trim());

                Location location = locationRepository.findById(locationId)
                        .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));

                Show show = Show.builder()
                        .title(title)
                        .slug(generateSlug(title))
                        .posterUrl(posterUrl)
                        .bookable(bookable)
                        .price(price)
                        .description(description)
                        .location(location)
                        .createdAt(LocalDateTime.now())
                        .build();

                showRepository.save(show);
                importedCount++;
            }

            return importedCount;

        } catch (IOException exception) {
            throw new BadRequestException("Could not read CSV file");
        } catch (NumberFormatException exception) {
            throw new BadRequestException("Invalid number format in CSV file");
        }
    }

    public String exportShows() {
        List<Show> shows = showRepository.findAll();

        StringBuilder builder = new StringBuilder();

        builder.append("id,title,posterUrl,bookable,price,description,locationId,locationDesignation\n");

        for (Show show : shows) {
            builder.append(safe(show.getId())).append(",");
            builder.append(escape(show.getTitle())).append(",");
            builder.append(escape(show.getPosterUrl())).append(",");
            builder.append(safe(show.getBookable())).append(",");
            builder.append(safe(show.getPrice())).append(",");
            builder.append(escape(show.getDescription())).append(",");
            builder.append(show.getLocation() != null ? safe(show.getLocation().getId()) : "").append(",");
            builder.append(show.getLocation() != null ? escape(show.getLocation().getDesignation()) : "").append("\n");
        }

        return builder.toString();
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

    private String escape(Object value) {
        if (value == null) {
            return "";
        }

        String text = value.toString().replace("\"", "\"\"");

        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            return "\"" + text + "\"";
        }

        return text;
    }

    private String safe(Object value) {
        return value == null ? "" : value.toString();
    }
}