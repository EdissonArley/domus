package com.domus.challenge.client;

import com.domus.challenge.exception.MovieApiException;
import com.domus.challenge.model.MovieResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovieClientImpl implements IMovieClient {

    private final WebClient.Builder webClientBuilder;
    private final String baseUrl;

    @Autowired
    public MovieClientImpl(@Value("${movies.api.base-url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.baseUrl = baseUrl;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<MovieResponse> fetchMoviesByPage(int page) {
        return webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder.queryParam("page", page).build())
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new MovieApiException("Movie API error: " + body)))
                )
                .bodyToMono(MovieResponse.class)
                .retry(3)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Movie API returned error status: {}", ex.getStatusCode());
                    return Mono.error(new MovieApiException("Movie API exception: " + ex.getResponseBodyAsString()));
                })
                .onErrorResume(WebClientRequestException.class, ex -> {
                    log.error("WebClient request failed: {}", ex.getMessage());
                    return Mono.error(new MovieApiException("Connection error: " + ex.getMessage()));
                })
                .onErrorResume(Throwable.class, ex -> {
                    log.error("Unexpected error while calling movie API", ex);
                    return Mono.error(new MovieApiException("Unexpected error: " + ex.getMessage()));
                });
    }
}
