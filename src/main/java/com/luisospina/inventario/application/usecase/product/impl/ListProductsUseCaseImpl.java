package com.luisospina.inventario.application.usecase.product.impl;

import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.application.usecase.product.ListProductsUseCase;
import com.luisospina.inventario.domain.product.ProductDomain;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ListProductsUseCaseImpl implements ListProductsUseCase {

    private final ProductRepository productRepository;

    public ListProductsUseCaseImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Flux<ProductDomain> execute() {
        return productRepository.findAllProducts();
    }
}
