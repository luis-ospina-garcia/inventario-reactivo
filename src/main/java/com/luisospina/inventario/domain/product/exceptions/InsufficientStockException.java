package com.luisospina.inventario.domain.product.exceptions;

import com.luisospina.inventario.crosscutting.exception.DomainException;
import com.luisospina.inventario.domain.product.ProductError;
import java.io.Serial;

public class InsufficientStockException extends DomainException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int availableStock;
    private final int requiredQuantity;

    private InsufficientStockException(String sku, int availableStock, int requiredQuantity) {
        super(ProductError.INSUFFICIENT_STOCK, sku, availableStock, requiredQuantity);
        this.availableStock = availableStock;
        this.requiredQuantity = requiredQuantity;
    }

    public static InsufficientStockException create(String sku, int availableStock, int requiredQuantity) {
        return new InsufficientStockException(sku, availableStock, requiredQuantity);
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }
}
