package com.domus.challenge.controller;

import com.domus.challenge.dto.DirectorsResponse;
import com.domus.challenge.service.IDirectorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class DirectorController {

    private final IDirectorService directorService;

    @GetMapping("/directors")
    @Operation(summary = "List directors with more than N movies",
            description = "Returns a list of directors who have directed more movies than the specified threshold, sorted alphabetically.")
    public Mono<ResponseEntity<DirectorsResponse>> getDirectors(@RequestParam("threshold") int threshold) {

        if (threshold < 0) {
            return Mono.just(ResponseEntity.ok(new DirectorsResponse(List.of())));
        }

        return directorService.getDirectorsAboveThreshold(threshold)
                .map(directors -> ResponseEntity.ok(new DirectorsResponse(directors)));
    }
}
