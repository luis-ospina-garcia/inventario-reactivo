package com.luisospina.inventario.application.usecase.product.impl;

import com.luisospina.inventario.application.secondaryports.repository.MovementRepository;
import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.application.usecase.product.CreateProductUseCase;
import com.luisospina.inventario.application.usecase.product.rulesvalidator.CreateProductRulesValidator;
import com.luisospina.inventario.domain.enums.MovementType;
import com.luisospina.inventario.domain.movement.MovementDomain;
import com.luisospina.inventario.domain.movement.vo.Quantity;
import com.luisospina.inventario.domain.product.ProductDomain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;
    private final CreateProductRulesValidator rulesValidator;

    public CreateProductUseCaseImpl(
            ProductRepository productRepository,
            MovementRepository movementRepository,
            CreateProductRulesValidator rulesValidator) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.rulesValidator = rulesValidator;
    }

    @Override
    @Transactional
    public Mono<ProductDomain> execute(ProductDomain product) {
        return rulesValidator.validate(product)
                .then(Mono.defer(() -> productRepository.saveProduct(product)))
                .flatMap(this::registerInitialStock);
    }

    /** El stock inicial queda en el historial como una primera entrada, para que el kárdex cuadre. */
    private Mono<ProductDomain> registerInitialStock(ProductDomain saved) {
        if (!saved.hasInitialStock()) {
            return Mono.just(saved);
        }
        MovementDomain initial = MovementDomain.create(
                saved.getId(), MovementType.IN, new Quantity(saved.getStock()), "Stock inicial");
        return movementRepository.saveMovement(initial).thenReturn(saved);
    }
}
