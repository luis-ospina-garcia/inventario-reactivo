package com.luisospina.inventario.application.secondaryports.repository;

import com.luisospina.inventario.domain.movement.MovementDomain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementRepository {

    Mono<MovementDomain> saveMovement(MovementDomain movement);

    Flux<MovementDomain> findMovementsByProductId(Long productId);
}
