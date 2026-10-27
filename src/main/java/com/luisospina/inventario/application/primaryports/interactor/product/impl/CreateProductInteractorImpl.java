package com.luisospina.inventario.application.primaryports.interactor.product.impl;

import com.luisospina.inventario.application.primaryports.dto.product.request.CreateProductRequest;
import com.luisospina.inventario.application.primaryports.dto.product.response.CreateProductResponse;
import com.luisospina.inventario.application.primaryports.interactor.product.CreateProductInteractor;
import com.luisospina.inventario.application.primaryports.mapper.product.ProductDTOMapper;
import com.luisospina.inventario.application.usecase.product.CreateProductUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateProductInteractorImpl implements CreateProductInteractor {

    private final CreateProductUseCase createProductUseCase;
    private final ProductDTOMapper productDTOMapper;

    public CreateProductInteractorImpl(
            CreateProductUseCase createProductUseCase, ProductDTOMapper productDTOMapper) {
        this.createProductUseCase = createProductUseCase;
        this.productDTOMapper = productDTOMapper;
    }

    @Override
    public Mono<CreateProductResponse> execute(CreateProductRequest request) {
        // defer: los invariantes del dominio se validan al construir el ProductDomain
        return Mono.defer(() -> createProductUseCase.execute(productDTOMapper.toDomain(request)))
                .map(productDTOMapper::toCreateProductResponse);
    }
}
