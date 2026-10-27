package com.luisospina.inventario.application.primaryports.dto.stock.response;

import com.luisospina.inventario.domain.enums.MovementType;
import java.time.LocalDateTime;

public record StockUpdatedResponse(
        Long productId,
        String sku,
        MovementType type,
        int quantity,
        int currentStock,
        LocalDateTime occurredAt) {
}
