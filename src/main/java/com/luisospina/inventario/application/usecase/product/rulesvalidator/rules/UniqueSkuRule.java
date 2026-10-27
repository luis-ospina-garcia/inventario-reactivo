package com.luisospina.inventario.application.usecase.product.rulesvalidator.rules;

import com.luisospina.inventario.application.usecase.Rule;
import com.luisospina.inventario.domain.product.ProductDomain;

/** El SKU no puede estar en uso por otro producto. */
public interface UniqueSkuRule extends Rule<ProductDomain> {
}
