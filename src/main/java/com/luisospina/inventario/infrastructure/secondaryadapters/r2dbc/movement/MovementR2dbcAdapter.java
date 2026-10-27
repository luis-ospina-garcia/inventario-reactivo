package com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.movement;

import com.luisospina.inventario.application.secondaryports.repository.MovementRepository;
import com.luisospina.inventario.domain.movement.MovementDomain;
import com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.mapper.MovementEntityMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MovementR2dbcAdapter implements MovementRepository {

    private final MovementR2dbcRepository movementR2dbcRepository;
    private final MovementEntityMapper movementEntityMapper;

    public MovementR2dbcAdapter(
            MovementR2dbcRepository movementR2dbcRepository, MovementEntityMapper movementEntityMapper) {
        this.movementR2dbcRepository = movementR2dbcRepository;
        this.movementEntityMapper = movementEntityMapper;
    }

    @Override
    public Mono<MovementDomain> saveMovement(MovementDomain movement) {
        return movementR2dbcRepository.save(movementEntityMapper.toEntity(movement))
                .map(movementEntityMapper::toDomain);
    }

    @Override
    public Flux<MovementDomain> findMovementsByProductId(Long productId) {
        return movementR2dbcRepository.findByProductIdOrderByIdAsc(productId)
                .map(movementEntityMapper::toDomain);
    }
}
