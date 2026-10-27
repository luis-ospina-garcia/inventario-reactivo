package com.luisospina.inventario.infrastructure.primaryadapters.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.luisospina.inventario.application.primaryports.dto.movement.request.CreateMovementRequest;
import com.luisospina.inventario.application.primaryports.dto.product.request.CreateProductRequest;
import com.luisospina.inventario.application.primaryports.dto.product.response.CreateProductResponse;
import com.luisospina.inventario.application.primaryports.interactor.movement.CreateMovementInteractor;
import com.luisospina.inventario.application.primaryports.interactor.product.CreateProductInteractor;
import com.luisospina.inventario.application.primaryports.interactor.product.GetProductByIdInteractor;
import com.luisospina.inventario.application.primaryports.interactor.stock.StreamStockInteractor;
import com.luisospina.inventario.domain.enums.MovementType;
import com.luisospina.inventario.domain.product.exceptions.InsufficientStockException;
import com.luisospina.inventario.infrastructure.primaryadapters.adapter.response.ApiResponse;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/** Integración sobre H2 en memoria: R2DBC y Flyway reales, sin Docker. */
@SpringBootTest
@AutoConfigureWebTestClient
class InventoryIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private CreateProductInteractor createProductInteractor;

    @Autowired
    private CreateMovementInteractor createMovementInteractor;

    @Autowired
    private GetProductByIdInteractor getProductByIdInteractor;

    @Autowired
    private StreamStockInteractor streamStockInteractor;

    private static String uniqueSku() {
        return "SKU-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private CreateProductResponse createProduct(String sku, int initialStock) {
        ApiResponse<CreateProductResponse> body = client.post().uri("/api/v1/products")
                .bodyValue(new CreateProductRequest(sku, "Producto de prueba", initialStock))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<ApiResponse<CreateProductResponse>>() { })
                .returnResult()
                .getResponseBody();
        return body.getData().getFirst();
    }

    @Test
    void flujo_completo_crea_producto_registra_movimientos_y_rechaza_salida_sin_stock() {
        CreateProductResponse product = createProduct(uniqueSku(), 10);

        client.post().uri("/api/v1/movements")
                .bodyValue(new CreateMovementRequest(product.id(), MovementType.OUT, 3, "Venta"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data[0].currentStock").isEqualTo(7);

        client.get().uri("/api/v1/products/{id}", product.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data[0].stock").isEqualTo(7);

        client.post().uri("/api/v1/movements")
                .bodyValue(new CreateMovementRequest(product.id(), MovementType.OUT, 100, "Venta grande"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.code").isEqualTo("PRODUCT_INSUFFICIENT_STOCK");

        // Historial: el stock inicial más la salida de 3. La salida rechazada no deja rastro.
        client.get().uri("/api/v1/movements?productId={id}", product.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.length()").isEqualTo(2);
    }

    @Test
    void sku_duplicado_responde_409() {
        String sku = uniqueSku();
        createProduct(sku, 1);

        client.post().uri("/api/v1/products")
                .bodyValue(new CreateProductRequest(sku, "Otro nombre", 1))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.code").isEqualTo("PRODUCT_DUPLICATE_SKU");
    }

    @Test
    void cantidad_invalida_responde_400_con_error_de_validacion() {
        CreateProductResponse product = createProduct(uniqueSku(), 5);

        client.post().uri("/api/v1/movements")
                .bodyValue(new CreateMovementRequest(product.id(), MovementType.IN, 0, null))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("VALIDATION_ERROR");
    }

    @Test
    void producto_inexistente_responde_404_al_consultar_y_al_mover_stock() {
        client.get().uri("/api/v1/products/{id}", 999_999)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PRODUCT_NOT_FOUND");

        client.post().uri("/api/v1/movements")
                .bodyValue(new CreateMovementRequest(999_999L, MovementType.IN, 1, null))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void salidas_concurrentes_nunca_dejan_el_stock_negativo() {
        CreateProductResponse product = createProductInteractor
                .execute(new CreateProductRequest(uniqueSku(), "Concurrente", 10))
                .block(Duration.ofSeconds(10));

        // 20 salidas de 1 unidad lanzadas en paralelo sobre un stock de 10
        List<Boolean> results = Flux.range(1, 20)
                .flatMap(i -> createMovementInteractor
                        .execute(new CreateMovementRequest(product.id(), MovementType.OUT, 1, "Venta " + i))
                        .thenReturn(true)
                        .onErrorResume(InsufficientStockException.class, e -> Mono.just(false)), 20)
                .collectList()
                .block(Duration.ofSeconds(30));

        assertThat(results).filteredOn(ok -> ok).hasSize(10);
        assertThat(results).filteredOn(ok -> !ok).hasSize(10);

        StepVerifier.create(getProductByIdInteractor.execute(product.id()))
                .assertNext(p -> assertThat(p.stock()).isZero())
                .verifyComplete();
    }

    @Test
    void el_stream_emite_el_cambio_de_stock_una_vez_confirmada_la_transaccion() {
        CreateProductResponse product = createProductInteractor
                .execute(new CreateProductRequest(uniqueSku(), "Streaming", 5))
                .block(Duration.ofSeconds(10));

        StepVerifier.create(streamStockInteractor.execute(product.id()).take(1))
                .then(() -> createMovementInteractor
                        .execute(new CreateMovementRequest(product.id(), MovementType.OUT, 2, "Venta"))
                        .block(Duration.ofSeconds(10)))
                .assertNext(event -> {
                    assertThat(event.productId()).isEqualTo(product.id());
                    assertThat(event.currentStock()).isEqualTo(3);
                })
                .expectComplete()
                .verify(Duration.ofSeconds(10));
    }
}
