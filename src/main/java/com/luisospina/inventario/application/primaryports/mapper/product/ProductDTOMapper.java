package com.luisospina.inventario.application.primaryports.mapper.product;

import com.luisospina.inventario.application.primaryports.dto.product.request.CreateProductRequest;
import com.luisospina.inventario.application.primaryports.dto.product.response.CreateProductResponse;
import com.luisospina.inventario.application.primaryports.dto.product.response.GetProductResponse;
import com.luisospina.inventario.application.primaryports.dto.product.response.ListProductResponse;
import com.luisospina.inventario.domain.product.ProductDomain;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOMapper {

    public ProductDomain toDomain(CreateProductRequest request) {
        return ProductDomain.create(request.sku(), request.name(), request.initialStock());
    }

    public CreateProductResponse toCreateProductResponse(ProductDomain product) {
        return new CreateProductResponse(
                product.getId(), product.getSku().value(), product.getName(), product.getStock());
    }

    public GetProductResponse toGetProductResponse(ProductDomain product) {
        return new GetProductResponse(
                product.getId(), product.getSku().value(), product.getName(), product.getStock());
    }

    public ListProductResponse toListProductResponse(ProductDomain product) {
        return new ListProductResponse(
                product.getId(), product.getSku().value(), product.getName(), product.getStock());
    }
}
