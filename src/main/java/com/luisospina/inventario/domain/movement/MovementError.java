package com.luisospina.inventario.domain.movement;

import com.luisospina.inventario.crosscutting.exception.ErrorCode;

public enum MovementError implements ErrorCode {
    PRODUCT_REQUIRED("El producto del movimiento es requerido"),
    TYPE_REQUIRED("El tipo de movimiento es requerido"),
    INVALID_QUANTITY("La cantidad %d no es válida. Debe ser mayor que cero");

    private final String messageTemplate;

    MovementError(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @Override
    public String getCode() {
        return "MOVEMENT_" + name();
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }
}
