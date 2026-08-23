package com.pid.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PublicWorkflowIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void exposesUpcomingRepresentationsAsRss() throws Exception {
        mockMvc.perform(get("/api/rss/upcoming-representations"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/xml"));
    }

    @Test
    void affiliateCatalogueRequiresAnApiKey() throws Exception {
        mockMvc.perform(get("/api/affiliate/shows"))
                .andExpect(status().isBadRequest());
    }
}
