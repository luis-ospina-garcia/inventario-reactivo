package com.luisospina.inventario.application.primaryports.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Las anotaciones de jakarta.validation existen solo para responder 400 con detalle por campo.
 * Las invariantes reales viven en el dominio (Sku, ProductDomain) y se vuelven a comprobar allí.
 */
public record CreateProductRequest(
        @NotBlank @Size(max = 50) String sku,
        @NotBlank @Size(max = 200) String name,
        @PositiveOrZero int initialStock) {
}
