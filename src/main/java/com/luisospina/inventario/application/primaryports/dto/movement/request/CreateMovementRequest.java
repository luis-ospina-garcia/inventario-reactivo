package com.luisospina.inventario.application.primaryports.dto.movement.request;

import com.luisospina.inventario.domain.enums.MovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Las anotaciones de jakarta.validation existen solo para responder 400 con detalle por campo.
 * Las invariantes reales viven en el dominio (Quantity, MovementDomain) y se vuelven a comprobar allí.
 */
public record CreateMovementRequest(
        @NotNull Long productId,
        @NotNull MovementType type,
        @Positive int quantity,
        @Size(max = 200) String reason) {
}
