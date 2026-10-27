package com.luisospina.inventario.application.usecase.product.rulesvalidator.rules.impl;

import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.application.usecase.product.rulesvalidator.rules.UniqueSkuRule;
import com.luisospina.inventario.domain.product.ProductDomain;
import com.luisospina.inventario.domain.product.exceptions.DuplicateSkuException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UniqueSkuRuleImpl implements UniqueSkuRule {

    private final ProductRepository productRepository;

    public UniqueSkuRuleImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Void> validate(ProductDomain product) {
        String sku = product.getSku().value();
        return productRepository.existsProductBySku(sku)
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.<Void>error(DuplicateSkuException.create(sku))
                        : Mono.<Void>empty());
    }
}
