package com.luisospina.inventario.application.usecase.shared.rules.impl;

import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.application.usecase.shared.rules.ProductExistsRule;
import com.luisospina.inventario.domain.product.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductExistsRuleImpl implements ProductExistsRule {

    private final ProductRepository productRepository;

    public ProductExistsRuleImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Void> validate(Long productId) {
        return productRepository.findProductById(productId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(ProductNotFoundException.create(productId))))
                .then();
    }
}
