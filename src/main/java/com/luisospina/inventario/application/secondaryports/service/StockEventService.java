package com.luisospina.inventario.application.secondaryports.service;

import com.luisospina.inventario.domain.product.event.StockUpdated;
import reactor.core.publisher.Flux;

/** Puerto de salida hacia el bus de eventos de stock. */
public interface StockEventService {

    void publish(StockUpdated event);

    /** Flujo en caliente: cada suscriptor recibe los eventos publicados desde que se suscribe. */
    Flux<StockUpdated> stream();
}
