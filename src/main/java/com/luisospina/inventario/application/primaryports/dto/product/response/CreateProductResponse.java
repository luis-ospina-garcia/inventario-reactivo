package com.luisospina.inventario.application.primaryports.dto.product.response;

public record CreateProductResponse(Long id, String sku, String name, int stock) {
}
