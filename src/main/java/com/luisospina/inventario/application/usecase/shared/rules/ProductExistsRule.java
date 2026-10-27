package com.luisospina.inventario.application.usecase.shared.rules;

import com.luisospina.inventario.application.usecase.Rule;

/** El producto referenciado debe existir. Compartida por los módulos movement y stock. */
public interface ProductExistsRule extends Rule<Long> {
}
