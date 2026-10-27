package com.luisospina.inventario.application.primaryports.dto.product.response;

public record ListProductResponse(Long id, String sku, String name, int stock) {
}
