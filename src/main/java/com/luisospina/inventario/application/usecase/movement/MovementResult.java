package com.luisospina.inventario.application.usecase.movement;

import com.luisospina.inventario.domain.movement.MovementDomain;
import com.luisospina.inventario.domain.product.ProductDomain;

/** Resultado de registrar un movimiento: el movimiento guardado y el producto con su stock ya actualizado. */
public record MovementResult(MovementDomain movement, ProductDomain product) {
}
