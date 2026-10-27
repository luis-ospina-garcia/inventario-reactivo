package com.luisospina.inventario.application.primaryports.mapper.movement;

import com.luisospina.inventario.application.primaryports.dto.movement.request.CreateMovementRequest;
import com.luisospina.inventario.application.primaryports.dto.movement.response.CreateMovementResponse;
import com.luisospina.inventario.application.primaryports.dto.movement.response.ListMovementResponse;
import com.luisospina.inventario.application.usecase.movement.CreateMovementCommand;
import com.luisospina.inventario.application.usecase.movement.MovementResult;
import com.luisospina.inventario.domain.movement.MovementDomain;
import org.springframework.stereotype.Component;

@Component
public class MovementDTOMapper {

    public CreateMovementCommand toCommand(CreateMovementRequest request) {
        return new CreateMovementCommand(
                request.productId(), request.type(), request.quantity(), request.reason());
    }

    public CreateMovementResponse toCreateMovementResponse(MovementResult result) {
        MovementDomain movement = result.movement();
        return new CreateMovementResponse(
                movement.getId(),
                movement.getProductId(),
                movement.getType(),
                movement.getQuantity().value(),
                movement.getReason(),
                movement.getCreatedAt(),
                result.product().getStock());
    }

    public ListMovementResponse toListMovementResponse(MovementDomain movement) {
        return new ListMovementResponse(
                movement.getId(),
                movement.getProductId(),
                movement.getType(),
                movement.getQuantity().value(),
                movement.getReason(),
                movement.getCreatedAt());
    }
}
