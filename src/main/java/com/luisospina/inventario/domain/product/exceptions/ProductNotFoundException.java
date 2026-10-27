package com.luisospina.inventario.domain.product.exceptions;

import com.luisospina.inventario.crosscutting.exception.DomainException;
import com.luisospina.inventario.domain.product.ProductError;
import java.io.Serial;

public class ProductNotFoundException extends DomainException {

    @Serial
    private static final long serialVersionUID = 1L;

    private ProductNotFoundException(Long productId) {
        super(ProductError.NOT_FOUND, productId);
    }

    public static ProductNotFoundException create(Long productId) {
        return new ProductNotFoundException(productId);
    }
}
