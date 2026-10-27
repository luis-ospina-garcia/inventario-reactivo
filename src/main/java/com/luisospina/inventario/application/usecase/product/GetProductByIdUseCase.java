package com.luisospina.inventario.application.usecase.product;

import com.luisospina.inventario.application.usecase.UseCaseWithReturn;
import com.luisospina.inventario.domain.product.ProductDomain;

public interface GetProductByIdUseCase extends UseCaseWithReturn<Long, ProductDomain> {
}
