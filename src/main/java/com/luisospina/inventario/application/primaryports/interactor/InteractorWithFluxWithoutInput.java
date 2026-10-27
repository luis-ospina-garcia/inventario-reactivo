package com.luisospina.inventario.application.primaryports.interactor;

import reactor.core.publisher.Flux;

@FunctionalInterface
public interface InteractorWithFluxWithoutInput<O> {
    Flux<O> execute();
}
