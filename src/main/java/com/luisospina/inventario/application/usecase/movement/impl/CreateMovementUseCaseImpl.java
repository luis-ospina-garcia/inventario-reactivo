package com.luisospina.inventario.application.usecase.movement.impl;

import com.luisospina.inventario.application.secondaryports.repository.MovementRepository;
import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.application.usecase.movement.CreateMovementCommand;
import com.luisospina.inventario.application.usecase.movement.CreateMovementUseCase;
import com.luisospina.inventario.application.usecase.movement.MovementResult;
import com.luisospina.inventario.application.usecase.movement.rulesvalidator.CreateMovementRulesValidator;
import com.luisospina.inventario.domain.movement.MovementDomain;
import com.luisospina.inventario.domain.movement.vo.Quantity;
import com.luisospina.inventario.domain.product.exceptions.InsufficientStockException;
import com.luisospina.inventario.domain.product.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class CreateMovementUseCaseImpl implements CreateMovementUseCase {

    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;
    private final CreateMovementRulesValidator rulesValidator;

    public CreateMovementUseCaseImpl(
            ProductRepository productRepository,
            MovementRepository movementRepository,
            CreateMovementRulesValidator rulesValidator) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.rulesValidator = rulesValidator;
    }

    @Override
    @Transactional
    public Mono<MovementResult> execute(CreateMovementCommand command) {
        // defer: si el dominio rechaza los datos (cantidad <= 0...), llega como señal de error
        return Mono.defer(() -> {
            MovementDomain movement = MovementDomain.create(
                    command.productId(), command.type(), new Quantity(command.quantity()), command.reason());

            return rulesValidator.validate(movement)
                    // UPDATE condicional atómico: dos salidas concurrentes nunca dejan el stock negativo
                    .then(Mono.defer(() -> productRepository.adjustStock(movement.getProductId(), movement.delta())))
                    .flatMap(applied -> saveIfApplied(applied, movement))
                    .flatMap(saved -> productRepository.findProductById(saved.getProductId())
                            .map(product -> new MovementResult(saved, product)));
        });
    }

    /** Si el UPDATE condicional afectó la fila, el movimiento se registra; si no, es que faltó stock. */
    private Mono<MovementDomain> saveIfApplied(Boolean applied, MovementDomain movement) {
        return Boolean.TRUE.equals(applied)
                ? movementRepository.saveMovement(movement)
                : rejectForInsufficientStock(movement);
    }

    /** El ajuste no se aplicó: el producto existe (lo validó la regla), así que es que no alcanza el stock. */
    private Mono<MovementDomain> rejectForInsufficientStock(MovementDomain movement) {
        return productRepository.findProductById(movement.getProductId())
                .switchIfEmpty(Mono.defer(() -> Mono.error(ProductNotFoundException.create(movement.getProductId()))))
                .flatMap(product -> Mono.<MovementDomain>error(InsufficientStockException.create(
                        product.getSku().value(), product.getStock(), movement.getQuantity().value())));
    }
}
