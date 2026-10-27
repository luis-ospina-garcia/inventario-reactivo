package com.luisospina.inventario.domain.movement.vo;

import com.luisospina.inventario.domain.movement.MovementError;
import com.luisospina.inventario.domain.movement.exceptions.InvalidMovementException;

/** Value Object: cantidad de unidades de un movimiento. Siempre mayor que cero (invariante del dominio). */
public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw InvalidMovementException.create(MovementError.INVALID_QUANTITY, value);
        }
    }
}
