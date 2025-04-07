package com.groovify.vinylshopapi.integration;

import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.repositories.ArtistRepository;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.OrderRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private VinylRecordRepository vinylRecordRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Artist aretha;

    private final String validArtistUpdateJson = """
            {
                "name": "Aretha Franklin Updated",
                "isGroup": false,
                "debutDate": "1960-05-05",
                "countryOfOrigin": "USA",
                "popularity": 90
            }
        """;

    private final String invalidArtistUpdateJson = """
            {
                "name": "",
                "isGroup": null,
                "debutDate": null,
                "countryOfOrigin": "",
                "popularity": null
            }
        """;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        vinylRecordRepository.deleteAll();
        artistRepository.deleteAll();

        List<Artist> testArtists = List.of(
                new Artist("Aretha Franklin", false, LocalDate.of(1956, 1, 1), "United States", 85),
                new Artist("Coldplay", true, LocalDate.of(2000, 7, 10), "United Kingdom", 80),
                new Artist("Queen", true, LocalDate.of(1973, 7, 13), "United Kingdom", 85)
        );

        artistRepository.saveAll(testArtists);
        aretha = artistRepository.findAll().getFirst();
    }

    // Tests for get Artist by id
    @Test
    @WithMockUser()
    void givenExistingArtist_whenGetArtistById_thenReturnArtis() throws Exception {
        mockMvc.perform(get("/api/artists/{id}", aretha.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(aretha.getId()))
                .andExpect(jsonPath("$.name").value(aretha.getName()))
                .andExpect(jsonPath("$.isGroup").value(aretha.getIsGroup()))
                .andExpect(jsonPath("$.debutDate").value(aretha.getDebutDate().toString()))
                .andExpect(jsonPath("$.countryOfOrigin").value(aretha.getCountryOfOrigin()))
                .andExpect(jsonPath("$.popularity").value(aretha.getPopularity()));
    }

    @Test
    @WithMockUser(roles = {"USER", "EMPLOYEE"})
    void givenNonExistingArtist_whenGetArtistById_thenThrowRecordNotFoundException() throws Exception {
        mockMvc.perform(get("/api/artists/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("No artist found with id: " + 999)));
    }

    @Test
    void givenNonAuthenticatedUser_whenGetArtistById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/api/artists/{id}", aretha.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(containsString("You have to be logged in to access this resource.")));
    }


    // Tests for update Artist
    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void givenExistingArtist_whenUpdateArtist_thenReturnUpdatedArtist() throws Exception {
        mockMvc.perform(put("/api/artists/{id}", aretha.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validArtistUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(aretha.getId()))
                .andExpect(jsonPath("$.name").value("Aretha Franklin Updated"))
                .andExpect(jsonPath("$.isGroup").value(false))
                .andExpect(jsonPath("$.debutDate").value("1960-05-05"))
                .andExpect(jsonPath("$.countryOfOrigin").value("USA"))
                .andExpect(jsonPath("$.popularity").value(90));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void givenNonExistingArtist_whenUpdateArtist_thenThrowRecordNotFoundException() throws Exception {
        mockMvc.perform(put("/api/artists/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validArtistUpdateJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("No artist found with id: " + 999)));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void givenInvalidRequestBody_whenUpdateArtist_thenThrowBadRequestException() throws Exception {
        mockMvc.perform(put("/api/artists/{id}", aretha.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidArtistUpdateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    @WithMockUser()
    void givenOnlyRoleUser_whenUpdateArtist_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(put("/api/artists/{id}", aretha.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validArtistUpdateJson))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(containsString("You do not have the permission to access this resource.")));
    }

    @Test
    void givenNonAuthenticatedUser_whenUpdateArtist_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(put("/api/artists/{id}", aretha.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validArtistUpdateJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(containsString("You have to be logged in to access this resource.")));
    }

}
