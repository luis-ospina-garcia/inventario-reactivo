package com.luisospina.inventario.application.primaryports.interactor.product.impl;

import com.luisospina.inventario.application.primaryports.dto.product.response.ListProductResponse;
import com.luisospina.inventario.application.primaryports.interactor.product.ListProductsInteractor;
import com.luisospina.inventario.application.primaryports.mapper.product.ProductDTOMapper;
import com.luisospina.inventario.application.usecase.product.ListProductsUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ListProductsInteractorImpl implements ListProductsInteractor {

    private final ListProductsUseCase listProductsUseCase;
    private final ProductDTOMapper productDTOMapper;

    public ListProductsInteractorImpl(
            ListProductsUseCase listProductsUseCase, ProductDTOMapper productDTOMapper) {
        this.listProductsUseCase = listProductsUseCase;
        this.productDTOMapper = productDTOMapper;
    }

    @Override
    public Flux<ListProductResponse> execute() {
        return listProductsUseCase.execute().map(productDTOMapper::toListProductResponse);
    }
}
