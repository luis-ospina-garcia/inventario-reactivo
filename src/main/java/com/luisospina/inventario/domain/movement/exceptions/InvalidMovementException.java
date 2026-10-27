package com.luisospina.inventario.domain.movement.exceptions;

import com.luisospina.inventario.crosscutting.exception.DomainException;
import com.luisospina.inventario.domain.movement.MovementError;
import java.io.Serial;

public class InvalidMovementException extends DomainException {

    @Serial
    private static final long serialVersionUID = 1L;

    private InvalidMovementException(MovementError error, Object... args) {
        super(error, args);
    }

    public static InvalidMovementException create(MovementError error, Object... args) {
        return new InvalidMovementException(error, args);
    }
}
