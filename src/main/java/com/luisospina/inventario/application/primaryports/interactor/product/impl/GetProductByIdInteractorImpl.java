package com.luisospina.inventario.application.primaryports.interactor.product.impl;

import com.luisospina.inventario.application.primaryports.dto.product.response.GetProductResponse;
import com.luisospina.inventario.application.primaryports.interactor.product.GetProductByIdInteractor;
import com.luisospina.inventario.application.primaryports.mapper.product.ProductDTOMapper;
import com.luisospina.inventario.application.usecase.product.GetProductByIdUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetProductByIdInteractorImpl implements GetProductByIdInteractor {

    private final GetProductByIdUseCase getProductByIdUseCase;
    private final ProductDTOMapper productDTOMapper;

    public GetProductByIdInteractorImpl(
            GetProductByIdUseCase getProductByIdUseCase, ProductDTOMapper productDTOMapper) {
        this.getProductByIdUseCase = getProductByIdUseCase;
        this.productDTOMapper = productDTOMapper;
    }

    @Override
    public Mono<GetProductResponse> execute(Long productId) {
        return getProductByIdUseCase.execute(productId).map(productDTOMapper::toGetProductResponse);
    }
}
