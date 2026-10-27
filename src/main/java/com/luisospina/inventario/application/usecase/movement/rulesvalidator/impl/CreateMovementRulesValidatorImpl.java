package com.luisospina.inventario.application.usecase.movement.rulesvalidator.impl;

import com.luisospina.inventario.application.usecase.OrderedRulesValidator;
import com.luisospina.inventario.application.usecase.Rule;
import com.luisospina.inventario.application.usecase.movement.rulesvalidator.CreateMovementRulesValidator;
import com.luisospina.inventario.domain.movement.MovementDomain;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Nota de diseño: "hay stock suficiente" NO es una Rule. Comprobarlo antes de escribir dejaría una
 * ventana de carrera entre dos salidas concurrentes; esa garantía la da el UPDATE atómico del caso de uso.
 */
@Component
public class CreateMovementRulesValidatorImpl extends OrderedRulesValidator<MovementDomain>
        implements CreateMovementRulesValidator {

    public CreateMovementRulesValidatorImpl(List<Rule<MovementDomain>> rules) {
        super(rules);
    }
}
