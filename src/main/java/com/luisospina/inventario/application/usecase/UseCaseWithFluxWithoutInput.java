package com.luisospina.inventario.application.usecase;

import reactor.core.publisher.Flux;

@FunctionalInterface
public interface UseCaseWithFluxWithoutInput<O> {
    Flux<O> execute();
}
