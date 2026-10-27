package com.luisospina.inventario.domain.product.exceptions;

import com.luisospina.inventario.crosscutting.exception.DomainException;
import com.luisospina.inventario.domain.product.ProductError;
import java.io.Serial;

public class InvalidProductException extends DomainException {

    @Serial
    private static final long serialVersionUID = 1L;

    private InvalidProductException(ProductError error, Object... args) {
        super(error, args);
    }

    public static InvalidProductException create(ProductError error, Object... args) {
        return new InvalidProductException(error, args);
    }
}
