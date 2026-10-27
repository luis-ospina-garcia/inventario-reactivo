package com.luisospina.inventario.application.usecase;

import reactor.core.publisher.Mono;

/**
 * Regla de negocio granular con estado (consulta un repositorio). Completa vacío o falla con una excepción tipada.
 * Las invariantes que no necesitan consultar nada no van aquí sino en el dominio (value objects).
 */
@FunctionalInterface
public interface Rule<T> {
    Mono<Void> validate(T value);
}
