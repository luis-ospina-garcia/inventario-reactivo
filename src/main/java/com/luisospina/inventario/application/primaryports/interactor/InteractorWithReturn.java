package com.luisospina.inventario.application.primaryports.interactor;

import reactor.core.publisher.Mono;

/** Puerto de entrada: recibe un DTO y devuelve un DTO. Lo consume el controlador. */
@FunctionalInterface
public interface InteractorWithReturn<I, O> {
    Mono<O> execute(I input);
}
