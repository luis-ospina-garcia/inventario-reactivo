package com.luisospina.inventario.application.usecase.movement;

import com.luisospina.inventario.domain.enums.MovementType;

public record CreateMovementCommand(Long productId, MovementType type, int quantity, String reason) {
}
