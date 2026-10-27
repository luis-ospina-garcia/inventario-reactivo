package com.luisospina.inventario.application.usecase.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.luisospina.inventario.application.secondaryports.repository.MovementRepository;
import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.application.usecase.product.impl.CreateProductUseCaseImpl;
import com.luisospina.inventario.application.usecase.product.rulesvalidator.impl.CreateProductRulesValidatorImpl;
import com.luisospina.inventario.application.usecase.product.rulesvalidator.rules.impl.UniqueSkuRuleImpl;
import com.luisospina.inventario.domain.enums.MovementType;
import com.luisospina.inventario.domain.movement.MovementDomain;
import com.luisospina.inventario.domain.product.ProductDomain;
import com.luisospina.inventario.domain.product.exceptions.DuplicateSkuException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateProductUseCaseTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final MovementRepository movementRepository = mock(MovementRepository.class);

    // Se usa el validador real con su regla real: solo el repositorio es un doble
    private final CreateProductUseCaseImpl useCase = new CreateProductUseCaseImpl(
            productRepository,
            movementRepository,
            new CreateProductRulesValidatorImpl(List.of(new UniqueSkuRuleImpl(productRepository))));

    private void stubSave() {
        when(productRepository.saveProduct(any())).thenAnswer(invocation -> {
            ProductDomain p = invocation.getArgument(0);
            return Mono.just(new ProductDomain(1L, p.getSku(), p.getName(), p.getStock()));
        });
        when(movementRepository.saveMovement(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    }

    @Test
    void sku_duplicado_falla_y_no_guarda_nada() {
        when(productRepository.existsProductBySku("SKU-1")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(ProductDomain.create("SKU-1", "Tornillo", 5)))
                .expectError(DuplicateSkuException.class)
                .verify();

        verify(productRepository, never()).saveProduct(any());
        verifyNoInteractions(movementRepository);
    }

    @Test
    void con_stock_inicial_registra_una_entrada_en_el_historial() {
        when(productRepository.existsProductBySku("SKU-1")).thenReturn(Mono.just(false));
        stubSave();

        StepVerifier.create(useCase.execute(ProductDomain.create("SKU-1", "Tornillo", 10)))
                .assertNext(saved -> assertThat(saved.getId()).isEqualTo(1L))
                .verifyComplete();

        ArgumentCaptor<MovementDomain> captor = ArgumentCaptor.forClass(MovementDomain.class);
        verify(movementRepository).saveMovement(captor.capture());
        assertThat(captor.getValue().getType()).isEqualTo(MovementType.IN);
        assertThat(captor.getValue().getQuantity().value()).isEqualTo(10);
        assertThat(captor.getValue().getProductId()).isEqualTo(1L);
    }

    @Test
    void sin_stock_inicial_no_registra_movimiento() {
        when(productRepository.existsProductBySku("SKU-2")).thenReturn(Mono.just(false));
        stubSave();

        StepVerifier.create(useCase.execute(ProductDomain.create("SKU-2", "Tuerca", 0)))
                .expectNextCount(1)
                .verifyComplete();

        verifyNoInteractions(movementRepository);
    }
}
