package com.luisospina.inventario.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class OrderedRulesValidatorTest {

    private static final class Validator extends OrderedRulesValidator<String> {
        Validator(List<Rule<String>> rules) {
            super(rules);
        }
    }

    private final List<String> calls = new ArrayList<>();

    private Rule<String> passing(String name) {
        return value -> Mono.fromRunnable(() -> calls.add(name));
    }

    private Rule<String> failing(String name, RuntimeException error) {
        return value -> Mono.defer(() -> {
            calls.add(name);
            return Mono.<Void>error(error);
        });
    }

    @Test
    void ejecuta_todas_las_reglas_en_orden() {
        Validator validator = new Validator(List.of(passing("a"), passing("b"), passing("c")));

        StepVerifier.create(validator.validate("x")).verifyComplete();

        assertThat(calls).containsExactly("a", "b", "c");
    }

    @Test
    void se_detiene_en_el_primer_error_y_no_ejecuta_las_siguientes() {
        IllegalStateException first = new IllegalStateException("falla b");
        Validator validator = new Validator(List.of(
                passing("a"), failing("b", first), failing("c", new IllegalStateException("falla c"))));

        StepVerifier.create(validator.validate("x"))
                .expectErrorSatisfies(error -> assertThat(error).isSameAs(first))
                .verify();

        assertThat(calls).containsExactly("a", "b");
    }

    @Test
    void sin_reglas_completa_vacio() {
        StepVerifier.create(new Validator(List.of()).validate("x")).verifyComplete();
    }
}
