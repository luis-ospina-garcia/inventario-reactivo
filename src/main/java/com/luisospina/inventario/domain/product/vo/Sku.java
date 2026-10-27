package com.luisospina.inventario.domain.product.vo;

import com.luisospina.inventario.crosscutting.helpers.TextHelper;
import com.luisospina.inventario.domain.product.ProductError;
import com.luisospina.inventario.domain.product.exceptions.InvalidProductException;

/** Value Object: identificador comercial del producto. Se valida al construirse. */
public record Sku(String value) {

    public static final int MAX_LENGTH = 50;

    public Sku {
        value = TextHelper.applyTrim(value);
        if (TextHelper.isBlank(value)) {
            throw InvalidProductException.create(ProductError.SKU_EMPTY);
        }
        if (value.length() > MAX_LENGTH) {
            throw InvalidProductException.create(ProductError.SKU_TOO_LONG, MAX_LENGTH);
        }
    }
}
