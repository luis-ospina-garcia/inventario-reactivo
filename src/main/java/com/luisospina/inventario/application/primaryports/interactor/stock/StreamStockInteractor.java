package com.luisospina.inventario.application.primaryports.interactor.stock;

import com.luisospina.inventario.application.primaryports.dto.stock.response.StockUpdatedResponse;
import com.luisospina.inventario.application.primaryports.interactor.InteractorWithFluxReturn;

/** Entrada: id del producto a seguir. */
public interface StreamStockInteractor extends InteractorWithFluxReturn<Long, StockUpdatedResponse> {
}
