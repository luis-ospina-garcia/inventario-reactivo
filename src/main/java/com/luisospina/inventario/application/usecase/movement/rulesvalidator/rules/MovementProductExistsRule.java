package com.luisospina.inventario.application.usecase.movement.rulesvalidator.rules;

import com.luisospina.inventario.application.usecase.Rule;
import com.luisospina.inventario.domain.movement.MovementDomain;

/** El producto al que se refiere el movimiento debe existir. */
public interface MovementProductExistsRule extends Rule<MovementDomain> {
}
