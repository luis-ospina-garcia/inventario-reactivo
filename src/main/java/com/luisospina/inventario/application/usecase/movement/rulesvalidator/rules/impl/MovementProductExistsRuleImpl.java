package com.luisospina.inventario.application.usecase.movement.rulesvalidator.rules.impl;

import com.luisospina.inventario.application.usecase.movement.rulesvalidator.rules.MovementProductExistsRule;
import com.luisospina.inventario.application.usecase.shared.rules.ProductExistsRule;
import com.luisospina.inventario.domain.movement.MovementDomain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MovementProductExistsRuleImpl implements MovementProductExistsRule {

    private final ProductExistsRule productExistsRule;

    public MovementProductExistsRuleImpl(ProductExistsRule productExistsRule) {
        this.productExistsRule = productExistsRule;
    }

    @Override
    public Mono<Void> validate(MovementDomain movement) {
        return productExistsRule.validate(movement.getProductId());
    }
}
