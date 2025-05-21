package com.domus.challenge.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IDirectorService {
    Mono<List<String>> getDirectorsAboveThreshold (int threshold);
}
