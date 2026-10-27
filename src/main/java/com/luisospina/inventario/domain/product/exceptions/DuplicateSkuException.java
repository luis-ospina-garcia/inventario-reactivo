package com.luisospina.inventario.domain.product.exceptions;

import com.luisospina.inventario.crosscutting.exception.DomainException;
import com.luisospina.inventario.domain.product.ProductError;
import java.io.Serial;

public class DuplicateSkuException extends DomainException {

    @Serial
    private static final long serialVersionUID = 1L;

    private DuplicateSkuException(String sku) {
        super(ProductError.DUPLICATE_SKU, sku);
    }

    public static DuplicateSkuException create(String sku) {
        return new DuplicateSkuException(sku);
    }
}
