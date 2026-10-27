package com.luisospina.inventario.application.usecase.stock;

import com.luisospina.inventario.application.usecase.UseCaseWithFluxReturn;
import com.luisospina.inventario.domain.product.event.StockUpdated;

/** Entrada: id del producto a seguir. */
public interface StreamStockUseCase extends UseCaseWithFluxReturn<Long, StockUpdated> {
}
