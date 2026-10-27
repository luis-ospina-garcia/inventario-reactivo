package com.luisospina.inventario.domain.movement;

import com.luisospina.inventario.domain.BaseDomain;
import com.luisospina.inventario.domain.enums.MovementType;
import com.luisospina.inventario.domain.movement.exceptions.InvalidMovementException;
import com.luisospina.inventario.domain.movement.vo.Quantity;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MovementDomain extends BaseDomain {

    private final Long productId;
    private final MovementType type;
    private final Quantity quantity;
    private final String reason;
    private final LocalDateTime createdAt;

    public MovementDomain(
            Long id, Long productId, MovementType type, Quantity quantity, String reason, LocalDateTime createdAt) {
        super(id);
        if (productId == null) {
            throw InvalidMovementException.create(MovementError.PRODUCT_REQUIRED);
        }
        if (type == null) {
            throw InvalidMovementException.create(MovementError.TYPE_REQUIRED);
        }
        if (quantity == null) {
            throw InvalidMovementException.create(MovementError.INVALID_QUANTITY, 0);
        }
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    /** Movimiento nuevo, todavía sin id; la fecha se fija en UTC al crearse. */
    public static MovementDomain create(Long productId, MovementType type, Quantity quantity, String reason) {
        return new MovementDomain(null, productId, type, quantity, reason, LocalDateTime.now(ZoneOffset.UTC));
    }

    /** Variación de stock que produce este movimiento (positiva en entradas, negativa en salidas). */
    public int delta() {
        return type.delta(quantity.value());
    }

    public Long getProductId() {
        return productId;
    }

    public MovementType getType() {
        return type;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
