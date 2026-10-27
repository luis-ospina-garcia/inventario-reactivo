package com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.movement;

import com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.entity.MovementEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MovementR2dbcRepository extends ReactiveCrudRepository<MovementEntity, Long> {

    Flux<MovementEntity> findByProductIdOrderByIdAsc(Long productId);
}
