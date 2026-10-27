package com.luisospina.inventario.application.usecase;

import reactor.core.publisher.Mono;

/** Orquesta, en orden, las {@link Rule} de un caso de uso. */
@FunctionalInterface
public interface RulesValidator<T> {
    Mono<Void> validate(T value);
}
