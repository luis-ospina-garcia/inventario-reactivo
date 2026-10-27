package com.luisospina.inventario.application.usecase.product.impl;

import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.application.usecase.product.GetProductByIdUseCase;
import com.luisospina.inventario.domain.product.ProductDomain;
import com.luisospina.inventario.domain.product.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetProductByIdUseCaseImpl implements GetProductByIdUseCase {

    private final ProductRepository productRepository;

    public GetProductByIdUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<ProductDomain> execute(Long productId) {
        return productRepository.findProductById(productId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(ProductNotFoundException.create(productId))));
    }
}
