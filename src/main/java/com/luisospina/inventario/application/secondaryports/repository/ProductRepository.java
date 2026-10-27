package com.luisospina.inventario.application.secondaryports.repository;

import com.luisospina.inventario.domain.product.ProductDomain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<ProductDomain> saveProduct(ProductDomain product);

    Mono<ProductDomain> findProductById(Long productId);

    Flux<ProductDomain> findAllProducts();

    Mono<Boolean> existsProductBySku(String sku);

    /**
     * Aplica la variación de stock de forma atómica en la base de datos.
     *
     * @return {@code true} si se aplicó; {@code false} si el producto no existe o el stock resultante
     *         sería negativo.
     */
    Mono<Boolean> adjustStock(Long productId, int delta);
}
