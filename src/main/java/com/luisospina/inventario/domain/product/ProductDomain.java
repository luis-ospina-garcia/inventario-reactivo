package com.luisospina.inventario.domain.product;

import com.luisospina.inventario.crosscutting.helpers.TextHelper;
import com.luisospina.inventario.domain.BaseDomain;
import com.luisospina.inventario.domain.product.exceptions.InvalidProductException;
import com.luisospina.inventario.domain.product.vo.Sku;

public class ProductDomain extends BaseDomain {

    private final Sku sku;
    private final String name;
    private final int stock;

    public ProductDomain(Long id, Sku sku, String name, int stock) {
        super(id);
        if (sku == null) {
            throw InvalidProductException.create(ProductError.SKU_EMPTY);
        }
        String cleanName = TextHelper.applyTrim(name);
        if (TextHelper.isBlank(cleanName)) {
            throw InvalidProductException.create(ProductError.NAME_REQUIRED);
        }
        if (stock < 0) {
            throw InvalidProductException.create(ProductError.NEGATIVE_STOCK);
        }
        this.sku = sku;
        this.name = cleanName;
        this.stock = stock;
    }

    /** Producto nuevo, todavía sin id (lo asigna la base de datos al persistirse). */
    public static ProductDomain create(String sku, String name, int initialStock) {
        return new ProductDomain(null, new Sku(sku), name, initialStock);
    }

    /** Un producto nuevo con stock mayor que cero debe dejar constancia en el historial. */
    public boolean hasInitialStock() {
        return stock > 0;
    }

    public Sku getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }
}
