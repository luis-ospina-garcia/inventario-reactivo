package com.luisospina.inventario.application.usecase;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Ejecuta las {@link Rule} en el orden recibido y se detiene en la primera que falle. Los validadores concretos
 * solo fijan el tipo; para añadir una regla basta crear otro {@code @Component} (y {@code @Order} si importa el orden).
 */
public abstract class OrderedRulesValidator<T> implements RulesValidator<T> {

    private final List<Rule<T>> rules;

    protected OrderedRulesValidator(List<Rule<T>> rules) {
        this.rules = List.copyOf(rules);
    }

    @Override
    public Mono<Void> validate(T value) {
        // concatMap suscribe a una regla a la vez: tras un error no se consulta ninguna de las siguientes
        return Flux.fromIterable(rules)
                .concatMap(rule -> rule.validate(value))
                .then();
    }
}
