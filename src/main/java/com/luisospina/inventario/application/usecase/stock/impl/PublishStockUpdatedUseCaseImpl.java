package com.luisospina.inventario.application.usecase.stock.impl;

import com.luisospina.inventario.application.secondaryports.service.StockEventService;
import com.luisospina.inventario.application.usecase.movement.MovementResult;
import com.luisospina.inventario.application.usecase.stock.PublishStockUpdatedUseCase;
import com.luisospina.inventario.domain.product.event.StockUpdated;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PublishStockUpdatedUseCaseImpl implements PublishStockUpdatedUseCase {

    private final StockEventService stockEventService;

    public PublishStockUpdatedUseCaseImpl(StockEventService stockEventService) {
        this.stockEventService = stockEventService;
    }

    @Override
    public Mono<Void> execute(MovementResult result) {
        return Mono.fromRunnable(() -> stockEventService.publish(new StockUpdated(
                result.product().getId(),
                result.product().getSku().value(),
                result.movement().getType(),
                result.movement().getQuantity().value(),
                result.product().getStock(),
                result.movement().getCreatedAt())));
    }
}
