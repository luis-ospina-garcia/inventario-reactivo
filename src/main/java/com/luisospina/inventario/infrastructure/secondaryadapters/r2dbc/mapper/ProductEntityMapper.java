package com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.mapper;

import com.luisospina.inventario.domain.product.ProductDomain;
import com.luisospina.inventario.domain.product.vo.Sku;
import com.luisospina.inventario.infrastructure.secondaryadapters.r2dbc.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(ProductDomain domain) {
        ProductEntity entity = new ProductEntity();
        entity.setId(domain.getId());
        entity.setSku(domain.getSku().value());
        entity.setName(domain.getName());
        entity.setStock(domain.getStock());
        return entity;
    }

    public ProductDomain toDomain(ProductEntity entity) {
        return new ProductDomain(entity.getId(), new Sku(entity.getSku()), entity.getName(), entity.getStock());
    }
}
