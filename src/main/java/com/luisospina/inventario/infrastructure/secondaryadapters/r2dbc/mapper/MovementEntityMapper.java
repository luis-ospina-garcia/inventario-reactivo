package com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.mapper;

import com.luisospina.inventario.domain.enums.MovementType;
import com.luisospina.inventario.domain.movement.MovementDomain;
import com.luisospina.inventario.domain.movement.vo.Quantity;
import com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.entity.MovementEntity;
import org.springframework.stereotype.Component;

@Component
public class MovementEntityMapper {

    public MovementEntity toEntity(MovementDomain domain) {
        MovementEntity entity = new MovementEntity();
        entity.setId(domain.getId());
        entity.setProductId(domain.getProductId());
        entity.setType(domain.getType().name());
        entity.setQuantity(domain.getQuantity().value());
        entity.setReason(domain.getReason());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public MovementDomain toDomain(MovementEntity entity) {
        return new MovementDomain(
                entity.getId(),
                entity.getProductId(),
                MovementType.valueOf(entity.getType()),
                new Quantity(entity.getQuantity()),
                entity.getReason(),
                entity.getCreatedAt());
    }
}
