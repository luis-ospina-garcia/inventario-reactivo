package com.luisospina.inventario.application.usecase.movement.impl;

import com.luisospina.inventario.application.secondaryports.repository.MovementRepository;
import com.luisospina.inventario.application.usecase.movement.ListMovementsByProductUseCase;
import com.luisospina.inventario.application.usecase.shared.rules.ProductExistsRule;
import com.luisospina.inventario.domain.movement.MovementDomain;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ListMovementsByProductUseCaseImpl implements ListMovementsByProductUseCase {

    private final MovementRepository movementRepository;
    private final ProductExistsRule productExistsRule;

    public ListMovementsByProductUseCaseImpl(
            MovementRepository movementRepository, ProductExistsRule productExistsRule) {
        this.movementRepository = movementRepository;
        this.productExistsRule = productExistsRule;
    }

    @Override
    public Flux<MovementDomain> execute(Long productId) {
        // Primero verifica que el producto exista; luego emite su historial en orden
        return productExistsRule.validate(productId)
                .thenMany(Flux.defer(() -> movementRepository.findMovementsByProductId(productId)));
    }
}
