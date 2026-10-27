package com.luisospina.inventario.application.primaryports.dto.movement.response;

import com.luisospina.inventario.domain.enums.MovementType;
import java.time.LocalDateTime;

public record ListMovementResponse(
        Long id,
        Long productId,
        MovementType type,
        int quantity,
        String reason,
        LocalDateTime createdAt) {
}
