package com.domus.challenge.client;

import com.domus.challenge.model.MovieResponse;
import reactor.core.publisher.Mono;

public interface IMovieClient {

    Mono<MovieResponse> fetchMoviesByPage (int page);
}
