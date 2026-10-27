package com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.product;

import com.luisospina.inventario.application.secondaryports.repository.ProductRepository;
import com.luisospina.inventario.domain.product.ProductDomain;
import com.luisospina.inventario.domain.product.exceptions.DuplicateSkuException;
import com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.mapper.ProductEntityMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductR2dbcAdapter implements ProductRepository {

    private final ProductR2dbcRepository productR2dbcRepository;
    private final ProductEntityMapper productEntityMapper;

    public ProductR2dbcAdapter(
            ProductR2dbcRepository productR2dbcRepository, ProductEntityMapper productEntityMapper) {
        this.productR2dbcRepository = productR2dbcRepository;
        this.productEntityMapper = productEntityMapper;
    }

    @Override
    public Mono<ProductDomain> saveProduct(ProductDomain product) {
        return productR2dbcRepository.save(productEntityMapper.toEntity(product))
                .map(productEntityMapper::toDomain)
                // La restricción UNIQUE del SKU es la garantía real frente a inserciones concurrentes
                .onErrorMap(DataIntegrityViolationException.class,
                        e -> DuplicateSkuException.create(product.getSku().value()));
    }

    @Override
    public Mono<ProductDomain> findProductById(Long productId) {
        return productR2dbcRepository.findById(productId).map(productEntityMapper::toDomain);
    }

    @Override
    public Flux<ProductDomain> findAllProducts() {
        return productR2dbcRepository.findAllByOrderByIdAsc().map(productEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsProductBySku(String sku) {
        return productR2dbcRepository.existsBySku(sku);
    }

    @Override
    public Mono<Boolean> adjustStock(Long productId, int delta) {
        return productR2dbcRepository.adjustStock(productId, delta).map(rows -> rows > 0);
    }
}
