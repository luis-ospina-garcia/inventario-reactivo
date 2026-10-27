package com.luisospina.inventario.application.usecase.product.rulesvalidator.impl;

import com.luisospina.inventario.application.usecase.OrderedRulesValidator;
import com.luisospina.inventario.application.usecase.Rule;
import com.luisospina.inventario.application.usecase.product.rulesvalidator.CreateProductRulesValidator;
import com.luisospina.inventario.domain.product.ProductDomain;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CreateProductRulesValidatorImpl extends OrderedRulesValidator<ProductDomain>
        implements CreateProductRulesValidator {

    public CreateProductRulesValidatorImpl(List<Rule<ProductDomain>> rules) {
        super(rules);
    }
}
