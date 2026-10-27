package com.luisospina.inventario.infrastructure.secondaryadapters.messaging;

import com.luisospina.inventario.domain.enums.MovementType;
import com.luisospina.inventario.domain.product.event.StockUpdated;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class SinkStockEventAdapterTest {

    private final SinkStockEventAdapter adapter = new SinkStockEventAdapter();

    private static StockUpdated event(long productId, int stock) {
        return new StockUpdated(productId, "SKU-" + productId, MovementType.OUT, 1, stock, LocalDateTime.now());
    }

    @Test
    void el_suscriptor_recibe_los_eventos_publicados_despues_de_suscribirse() {
        StockUpdated first = event(1, 9);
        StockUpdated second = event(1, 8);

        StepVerifier.create(adapter.stream().take(2))
                .then(() -> {
                    adapter.publish(first);
                    adapter.publish(second);
                })
                .expectNext(first, second)
                .verifyComplete();
    }

    @Test
    void publicar_sin_suscriptores_no_falla() {
        adapter.publish(event(1, 9));
    }
}
