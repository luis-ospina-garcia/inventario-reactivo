package com.luisospina.inventario.infrastructure.primaryadapters.adapter.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Arma la respuesta HTTP; los controladores solo delegan aquí, sin construir envoltorios a mano. */
public final class HttpResponses {

    private HttpResponses() {
    }

    public static <T> Mono<ResponseEntity<ApiResponse<T>>> created(Mono<T> body, String message) {
        return body.map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(dto, message)));
    }

    public static <T> Mono<ResponseEntity<ApiResponse<T>>> ok(Mono<T> body) {
        return body.map(dto -> ResponseEntity.ok(ApiResponse.of(dto)));
    }

    public static <T> Mono<ResponseEntity<ApiResponse<T>>> okList(Flux<T> body) {
        return body.collectList().map(list -> ResponseEntity.ok(ApiResponse.of(list)));
    }
}
