package com.luisospina.inventario.application.usecase.stock.impl;

import com.luisospina.inventario.application.secondaryports.service.StockEventService;
import com.luisospina.inventario.application.usecase.shared.rules.ProductExistsRule;
import com.luisospina.inventario.application.usecase.stock.StreamStockUseCase;
import com.luisospina.inventario.domain.product.event.StockUpdated;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StreamStockUseCaseImpl implements StreamStockUseCase {

    private final StockEventService stockEventService;
    private final ProductExistsRule productExistsRule;

    public StreamStockUseCaseImpl(StockEventService stockEventService, ProductExistsRule productExistsRule) {
        this.stockEventService = stockEventService;
        this.productExistsRule = productExistsRule;
    }

    @Override
    public Flux<StockUpdated> execute(Long productId) {
        Flux<StockUpdated> events = stockEventService.stream()
                .filter(event -> productId.equals(event.productId()));

        // merge suscribe a las dos fuentes a la vez: el bus ya está escuchando mientras se verifica que el
        // producto exista (así no se pierde ningún evento), y si no existe, el flujo termina con el error.
        Flux<StockUpdated> existenceCheck = productExistsRule.validate(productId)
                .thenMany(Flux.<StockUpdated>empty());

        return Flux.merge(events, existenceCheck);
    }
}
