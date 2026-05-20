package com.pid.backend.service;

import com.pid.backend.entity.Representation;
import com.pid.backend.repository.RepresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RssFeedService {

    private final RepresentationRepository representationRepository;

    public String generateUpcomingRepresentationsFeed() {
        List<Representation> representations = representationRepository.findAll()
                .stream()
                .filter(representation ->
                        representation.getPerformanceDate() != null
                                && !representation.getPerformanceDate().isBefore(LocalDate.now())
                )
                .toList();

        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        builder.append("<rss version=\"2.0\">");
        builder.append("<channel>");
        builder.append("<title>Upcoming Theatre Performances</title>");
        builder.append("<description>RSS feed for upcoming show performances</description>");
        builder.append("<link>http://localhost:8080</link>");

        for (Representation representation : representations) {
            String showTitle = representation.getShow() != null
                    ? representation.getShow().getTitle()
                    : "Untitled show";

            String location = representation.getLocation() != null
                    ? representation.getLocation().getDesignation()
                    : "Unknown location";

            builder.append("<item>");
            builder.append("<title>").append(escapeXml(showTitle)).append("</title>");
            builder.append("<description>")
                    .append(escapeXml("Performance at " + location + " on "
                            + representation.getPerformanceDate() + " "
                            + representation.getPerformanceTime()))
                    .append("</description>");
            builder.append("<guid>")
                    .append("representation-")
                    .append(representation.getId())
                    .append("</guid>");
            builder.append("</item>");
        }

        builder.append("</channel>");
        builder.append("</rss>");

        return builder.toString();
    }

    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}