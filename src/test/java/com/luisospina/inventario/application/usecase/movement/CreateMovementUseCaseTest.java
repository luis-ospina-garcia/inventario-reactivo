package com.luisospina.inventario.application.usecase.movement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.luisospina.inventario.application.secondaryports.repository.MovementRepository;
import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.application.usecase.movement.impl.CreateMovementUseCaseImpl;
import com.luisospina.inventario.application.usecase.movement.rulesvalidator.CreateMovementRulesValidator;
import com.luisospina.inventario.domain.enums.MovementType;
import com.luisospina.inventario.domain.movement.MovementDomain;
import com.luisospina.inventario.domain.movement.exceptions.InvalidMovementException;
import com.luisospina.inventario.domain.product.ProductDomain;
import com.luisospina.inventario.domain.product.exceptions.InsufficientStockException;
import com.luisospina.inventario.domain.product.exceptions.ProductNotFoundException;
import com.luisospina.inventario.domain.product.vo.Sku;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateMovementUseCaseTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final MovementRepository movementRepository = mock(MovementRepository.class);
    private final CreateMovementRulesValidator rulesValidator = mock(CreateMovementRulesValidator.class);

    private final CreateMovementUseCaseImpl useCase =
            new CreateMovementUseCaseImpl(productRepository, movementRepository, rulesValidator);

    @Test
    void salida_sin_stock_suficiente_falla_y_no_registra_el_movimiento() {
        when(rulesValidator.validate(any())).thenReturn(Mono.empty());
        when(productRepository.adjustStock(1L, -5)).thenReturn(Mono.just(false));
        when(productRepository.findProductById(1L))
                .thenReturn(Mono.just(new ProductDomain(1L, new Sku("SKU-1"), "Tornillo", 2)));

        StepVerifier.create(useCase.execute(new CreateMovementCommand(1L, MovementType.OUT, 5, "Venta")))
                .expectError(InsufficientStockException.class)
                .verify();

        verify(movementRepository, never()).saveMovement(any());
    }

    @Test
    void entrada_aplicada_guarda_el_movimiento_y_devuelve_el_stock_resultante() {
        when(rulesValidator.validate(any())).thenReturn(Mono.empty());
        when(productRepository.adjustStock(1L, 4)).thenReturn(Mono.just(true));
        when(movementRepository.saveMovement(any())).thenAnswer(invocation -> {
            MovementDomain m = invocation.getArgument(0);
            return Mono.just(new MovementDomain(
                    10L, m.getProductId(), m.getType(), m.getQuantity(), m.getReason(), m.getCreatedAt()));
        });
        when(productRepository.findProductById(1L))
                .thenReturn(Mono.just(new ProductDomain(1L, new Sku("SKU-1"), "Tornillo", 14)));

        StepVerifier.create(useCase.execute(new CreateMovementCommand(1L, MovementType.IN, 4, "Compra")))
                .assertNext(result -> {
                    assertThat(result.movement().getId()).isEqualTo(10L);
                    assertThat(result.product().getStock()).isEqualTo(14);
                })
                .verifyComplete();
    }

    @Test
    void producto_inexistente_falla_en_la_regla_y_no_toca_la_base_de_datos() {
        when(rulesValidator.validate(any())).thenReturn(Mono.error(ProductNotFoundException.create(99L)));

        StepVerifier.create(useCase.execute(new CreateMovementCommand(99L, MovementType.OUT, 1, null)))
                .expectError(ProductNotFoundException.class)
                .verify();

        verifyNoInteractions(productRepository, movementRepository);
    }

    @Test
    void cantidad_cero_falla_en_el_dominio_antes_de_cualquier_consulta() {
        StepVerifier.create(useCase.execute(new CreateMovementCommand(1L, MovementType.OUT, 0, null)))
                .expectError(InvalidMovementException.class)
                .verify();

        verifyNoInteractions(productRepository, movementRepository, rulesValidator);
    }
}
