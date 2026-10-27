package com.luisospina.inventario.application.primaryports.interactor.product;

import com.luisospina.inventario.application.primaryports.dto.product.response.GetProductResponse;
import com.luisospina.inventario.application.primaryports.interactor.InteractorWithReturn;

public interface GetProductByIdInteractor extends InteractorWithReturn<Long, GetProductResponse> {
}
