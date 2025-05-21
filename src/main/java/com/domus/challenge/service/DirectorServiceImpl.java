package com.domus.challenge.service;

import com.domus.challenge.client.IMovieClient;
import com.domus.challenge.model.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements IDirectorService {

    private final IMovieClient movieClient;
    @Override
    public Mono<List<String>> getDirectorsAboveThreshold(int threshold) {
        Map<String, Integer> countMap = new ConcurrentHashMap<>();

        return movieClient.fetchMoviesByPage(1).flatMapMany(movieResponse -> {
            int totalPages = movieResponse.getTotalPages();
            return Flux.range(1, totalPages)
                    .flatMap(movieClient::fetchMoviesByPage)
                    .flatMapIterable(MovieResponse::getData)
                    .doOnNext(movie -> {
                        String director = movie.getDirector();
                        if (director != null && !director.isBlank()) {
                            countMap.merge(director, 1, Integer::sum);
                        }
                    });
        }).then(Mono.fromCallable(() -> {
            Set<String> sortedDirectors = new TreeSet<>();
            countMap.forEach((director, count) -> {
                if (count > threshold) {
                    sortedDirectors.add(director);
                }
            });
            return new ArrayList<>(sortedDirectors);
        }));
    }
}
