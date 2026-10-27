package com.luisospina.inventario.domain.product.event;

import com.luisospina.inventario.domain.enums.MovementType;
import java.time.LocalDateTime;

/** Evento de dominio: el stock de un producto cambió tras un movimiento confirmado. */
public record StockUpdated(
        Long productId,
        String sku,
        MovementType type,
        int quantity,
        int currentStock,
        LocalDateTime occurredAt) {
}
