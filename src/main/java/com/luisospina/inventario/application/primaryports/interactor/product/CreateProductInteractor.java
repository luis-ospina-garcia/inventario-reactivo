package com.luisospina.inventario.application.primaryports.interactor.product;

import com.luisospina.inventario.application.primaryports.dto.product.request.CreateProductRequest;
import com.luisospina.inventario.application.primaryports.dto.product.response.CreateProductResponse;
import com.luisospina.inventario.application.primaryports.interactor.InteractorWithReturn;

public interface CreateProductInteractor extends InteractorWithReturn<CreateProductRequest, CreateProductResponse> {
}
