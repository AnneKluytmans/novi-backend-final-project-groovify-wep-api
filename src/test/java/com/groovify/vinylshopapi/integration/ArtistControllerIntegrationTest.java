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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @Test
    @WithMockUser()
    void givenExistingArtist_whenGetArtistById_thenReturnArtistResponseDTO() throws Exception {
        mockMvc.perform(get("/api/artists/{id}", aretha.getId())
                        .contentType(MediaType.APPLICATION_JSON))
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
}
