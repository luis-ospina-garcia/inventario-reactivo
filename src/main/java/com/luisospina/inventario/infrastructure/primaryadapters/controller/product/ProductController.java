package com.luisospina.inventario.infrastructure.primaryadapters.controller.product;

import com.luisospina.inventario.application.primaryports.dto.product.request.CreateProductRequest;
import com.luisospina.inventario.application.primaryports.dto.product.response.CreateProductResponse;
import com.luisospina.inventario.application.primaryports.dto.product.response.GetProductResponse;
import com.luisospina.inventario.application.primaryports.dto.product.response.ListProductResponse;
import com.luisospina.inventario.application.primaryports.dto.stock.response.StockUpdatedResponse;
import com.luisospina.inventario.application.primaryports.interactor.product.CreateProductInteractor;
import com.luisospina.inventario.application.primaryports.interactor.product.GetProductByIdInteractor;
import com.luisospina.inventario.application.primaryports.interactor.product.ListProductsInteractor;
import com.luisospina.inventario.application.primaryports.interactor.stock.StreamStockInteractor;
import com.luisospina.inventario.infrastructure.ResponseMessages;
import com.luisospina.inventario.infrastructure.primaryadapters.adapter.response.ApiResponse;
import com.luisospina.inventario.infrastructure.primaryadapters.adapter.response.HttpResponses;
import com.luisospina.inventario.infrastructure.primaryadapters.adapter.response.SseEvents;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductInteractor createProductInteractor;
    private final GetProductByIdInteractor getProductByIdInteractor;
    private final ListProductsInteractor listProductsInteractor;
    private final StreamStockInteractor streamStockInteractor;

    public ProductController(
            CreateProductInteractor createProductInteractor,
            GetProductByIdInteractor getProductByIdInteractor,
            ListProductsInteractor listProductsInteractor,
            StreamStockInteractor streamStockInteractor) {
        this.createProductInteractor = createProductInteractor;
        this.getProductByIdInteractor = getProductByIdInteractor;
        this.listProductsInteractor = listProductsInteractor;
        this.streamStockInteractor = streamStockInteractor;
    }

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<CreateProductResponse>>> createProduct(
            @RequestBody @Valid CreateProductRequest request) {
        return HttpResponses.created(createProductInteractor.execute(request), ResponseMessages.PRODUCT_CREATED);
    }

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<ListProductResponse>>> listProducts() {
        return HttpResponses.okList(listProductsInteractor.execute());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<GetProductResponse>>> getProduct(@PathVariable Long id) {
        return HttpResponses.ok(getProductByIdInteractor.execute(id));
    }

    /** Server-Sent Events: cada cambio de stock confirmado del producto llega al cliente al instante. */
    @GetMapping(value = "/{id}/stock/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<StockUpdatedResponse>> streamStock(@PathVariable Long id) {
        return SseEvents.map(streamStockInteractor.execute(id), SseEvents.STOCK);
    }
}
