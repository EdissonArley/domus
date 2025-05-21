package com.domus.challenge.service;

import com.domus.challenge.client.IMovieClient;
import com.domus.challenge.model.Movie;
import com.domus.challenge.model.MovieResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

public class DirectorServiceImplTest {

    private IMovieClient movieClient;
    private DirectorServiceImpl directorService;

    @BeforeEach
    void setUp() {
        movieClient = mock(IMovieClient.class);
        directorService = new DirectorServiceImpl(movieClient);
    }

    @Test
    void shouldReturnDirectorsAboveThreshold() {
        Movie movie1 = new Movie();
        movie1.setDirector("Woody Allen");
        Movie movie2 = new Movie();
        movie2.setDirector("Woody Allen");
        Movie movie3 = new Movie();
        movie3.setDirector("Quentin Tarantino");

        MovieResponse response = new MovieResponse();
        response.setTotalPages(1);
        response.setData(List.of(movie1, movie2, movie3));

        when(movieClient.fetchMoviesByPage(1)).thenReturn(Mono.just(response));

        StepVerifier.create(directorService.getDirectorsAboveThreshold(1))
                .expectNext(List.of("Woody Allen"))
                .verifyComplete();
    }
}
