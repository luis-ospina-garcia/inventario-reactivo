package com.luisospina.inventario.infrastructure.primaryadapters.adapter.response;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public final class SseEvents {

    public static final String STOCK = "stock";

    private SseEvents() {
    }

    public static <T> Flux<ServerSentEvent<T>> map(Flux<T> source, String eventName) {
        return source.map(data -> ServerSentEvent.<T>builder().event(eventName).data(data).build());
    }
}
