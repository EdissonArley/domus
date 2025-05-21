package com.domus.challenge.client;

import com.domus.challenge.model.MovieResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

public class MovieClientImplTest {

    private static MockWebServer mockWebServer;
    private MovieClientImpl movieClient;

    @BeforeAll
    static void setUpAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDownAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        WebClient.Builder builder = WebClient.builder();
        movieClient = new MovieClientImpl(mockWebServer.url("/").toString(), builder);
    }

    @Test
    void shouldFetchMoviesByPage() {
        String body = """
        {
          "page": 1,
          "per_page": 2,
          "total": 3,
          "total_pages": 2,
          "data": [
            {"Director": "Woody Allen", "Title": "Midnight in Paris"},
            {"Director": "Quentin Tarantino", "Title": "The Hateful Eigh"}
          ]
        }
        """;

        mockWebServer.enqueue(new MockResponse().setBody(body).addHeader("Content-Type", "application/json"));

        StepVerifier.create(movieClient.fetchMoviesByPage(1))
                .expectNextMatches(response -> response.getData().size() == 2)
                .verifyComplete();
    }
}
