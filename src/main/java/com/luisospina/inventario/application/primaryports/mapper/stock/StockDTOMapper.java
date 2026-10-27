package com.luisospina.inventario.application.primaryports.mapper.stock;

import com.luisospina.inventario.application.primaryports.dto.stock.response.StockUpdatedResponse;
import com.luisospina.inventario.domain.product.event.StockUpdated;
import org.springframework.stereotype.Component;

@Component
public class StockDTOMapper {

    public StockUpdatedResponse toStockUpdatedResponse(StockUpdated event) {
        return new StockUpdatedResponse(
                event.productId(),
                event.sku(),
                event.type(),
                event.quantity(),
                event.currentStock(),
                event.occurredAt());
    }
}
