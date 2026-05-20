package com.pid.backend.controller;

import com.pid.backend.service.RssFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rss")
@RequiredArgsConstructor
public class RssFeedController {

    private final RssFeedService rssFeedService;

    @GetMapping(value = "/upcoming-representations", produces = MediaType.APPLICATION_XML_VALUE)
    public String getUpcomingRepresentationsFeed() {
        return rssFeedService.generateUpcomingRepresentationsFeed();
    }
}