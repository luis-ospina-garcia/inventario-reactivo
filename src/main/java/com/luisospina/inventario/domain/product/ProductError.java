package com.luisospina.inventario.domain.product;

import com.luisospina.inventario.crosscutting.exception.ErrorCode;

public enum ProductError implements ErrorCode {
    SKU_EMPTY("El SKU no puede estar vacío"),
    SKU_TOO_LONG("El SKU no puede superar los %d caracteres"),
    NAME_REQUIRED("El nombre del producto es requerido"),
    NEGATIVE_STOCK("El stock no puede ser negativo"),
    NOT_FOUND("No se encontró el producto con id %s"),
    DUPLICATE_SKU("Ya existe un producto con el SKU %s"),
    INSUFFICIENT_STOCK("Stock insuficiente para el SKU %s: disponible=%d, requerido=%d");

    private final String messageTemplate;

    ProductError(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @Override
    public String getCode() {
        return "PRODUCT_" + name();
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }
}
