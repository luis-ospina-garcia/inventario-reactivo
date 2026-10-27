package com.luisospina.inventario.application.primaryports.dto.movement.response;

import com.luisospina.inventario.domain.enums.MovementType;
import java.time.LocalDateTime;

/** Movimiento registrado más el stock resultante del producto. */
public record CreateMovementResponse(
        Long id,
        Long productId,
        MovementType type,
        int quantity,
        String reason,
        LocalDateTime createdAt,
        int currentStock) {
}
