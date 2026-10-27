package com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.product;

import com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, Long> {

    Mono<Boolean> existsBySku(String sku);

    Flux<ProductEntity> findAllByOrderByIdAsc();

    /** Atómico: la condición del WHERE evita que el stock quede negativo bajo concurrencia. */
    @Modifying
    // La concurrencia se resuelve aquí, en la base de datos: validar antes en una Rule dejaría una ventana de carrera
    @Query("UPDATE product SET stock = stock + :delta WHERE id = :id AND stock + :delta >= 0")
    Mono<Integer> adjustStock(@Param("id") Long id, @Param("delta") int delta);
}
