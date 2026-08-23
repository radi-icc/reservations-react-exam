package com.pid.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import com.pid.backend.entity.*;
import com.pid.backend.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PublicWorkflowIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private ShowRepository showRepository;
    @Autowired private RepresentationRepository representationRepository;
    @Autowired private PriceRepository priceRepository;

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

    @Test
    @WithMockUser(username = "booking-member", roles = "MEMBER")
    void createsAConfirmedReservationForAnAuthenticatedMember() throws Exception {
        User member = member("booking-member");
        Location location = location("Booking hall");
        Show show = show("Booking show", location);
        Representation representation = representation(show, location, LocalDate.now().plusDays(7));
        Price price = price(representation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"representationId\":" + representation.getId() + ",\"priceId\":" + price.getId() + ",\"quantity\":2,\"ticketDeliveryMethod\":\"EMAIL\",\"paymentMethod\":\"CARD\"}"))
                .andExpect(status().isCreated())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser(username = "review-member", roles = "MEMBER")
    void rejectsAReviewWhenTheMemberDidNotAttend() throws Exception {
        member("review-member");
        Location location = location("Review hall");
        Show show = show("Review show", location);

        mockMvc.perform(post("/api/reviews").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"showId\":" + show.getId() + ",\"rating\":5,\"comment\":\"Great\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void importsShowsFromCsvForAnAdministrator() throws Exception {
        Location location = location("CSV hall");
        MockMultipartFile file = new MockMultipartFile("file", "shows.csv", "text/csv",
                ("title,posterUrl,bookable,price,description,locationId\nCSV show,,true,12.50,Imported," + location.getId()).getBytes());
        mockMvc.perform(multipart("/api/admin/csv/shows/import").file(file))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.importedCount").value(1));
    }

    private User member(String username) {
        Role role = roleRepository.findByRoleName("MEMBER")
                .orElseGet(() -> roleRepository.save(Role.builder().roleName("MEMBER").build()));
        return userRepository.save(User.builder().username(username).email(username + "@example.test").password("hashed").role(role).build());
    }
    private Location location(String designation) { return locationRepository.save(Location.builder().designation(designation).build()); }
    private Show show(String title, Location location) { return showRepository.save(Show.builder().title(title).slug(title.toLowerCase().replace(" ", "-")).location(location).bookable(true).price(BigDecimal.TEN).build()); }
    private Representation representation(Show show, Location location, LocalDate date) { return representationRepository.save(Representation.builder().show(show).location(location).performanceDate(date).performanceTime(LocalTime.of(20, 0)).capacity(50).bookedSeats(0).build()); }
    private Price price(Representation representation) { return priceRepository.save(Price.builder().label("Standard").amount(BigDecimal.TEN).representation(representation).build()); }
}
