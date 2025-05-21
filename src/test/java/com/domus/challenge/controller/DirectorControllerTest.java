package com.domus.challenge.controller;

import com.domus.challenge.dto.DirectorsResponse;
import com.domus.challenge.service.IDirectorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(DirectorController.class)
public class DirectorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IDirectorService directorService;

    @Test
    void shouldReturnListOfDirectorsWrappedInDto() {
        when(directorService.getDirectorsAboveThreshold(1))
                .thenReturn(Mono.just(List.of("Woody Allen", "Quentin Tarantino")));

        webTestClient.get()
                .uri("/api/directors?threshold=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(DirectorsResponse.class)
                .value(response -> {
                    assert response.getDirectors().size() == 2;
                    assert response.getDirectors().contains("Quentin Tarantino");
                });
    }

    @Test
    void shouldReturnEmptyListForNegativeThreshold() {
        webTestClient.get()
                .uri("/api/directors?threshold=-1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody()
                .jsonPath("$.directors").isEmpty();
    }

    @Test
    void shouldReturnBadRequestForMissingThreshold() {
        webTestClient.get()
                .uri("/api/directors")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
