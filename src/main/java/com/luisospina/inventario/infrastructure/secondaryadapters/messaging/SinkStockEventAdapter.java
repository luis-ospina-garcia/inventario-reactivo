package com.luisospina.inventario.infrastructure.secondaryadapters.messaging;

import com.luisospina.inventario.application.secondaryports.service.StockEventService;
import com.luisospina.inventario.domain.product.event.StockUpdated;
import java.time.Duration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Bus de eventos en memoria sobre un {@link Sinks.Many} multicast.
 *
 * <p>{@code directBestEffort}: si un suscriptor es lento, se le descartan eventos en vez de frenar al
 * productor ni al resto de suscriptores. Basta para un stream de "estado actual"; no sustituye a un
 * broker si hiciera falta entrega garantizada o varias réplicas de la aplicación.
 */
@Component
public class SinkStockEventAdapter implements StockEventService {

    private final Sinks.Many<StockUpdated> sink = Sinks.many().multicast().directBestEffort();

    @Override
    public void publish(StockUpdated event) {
        // busyLooping reintenta si dos hilos emiten a la vez (emisión no serializada)
        sink.emitNext(event, Sinks.EmitFailureHandler.busyLooping(Duration.ofMillis(100)));
    }

    @Override
    public Flux<StockUpdated> stream() {
        return sink.asFlux();
    }
}
