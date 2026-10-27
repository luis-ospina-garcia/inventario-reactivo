package com.luisospina.inventario.application.usecase.movement;

import com.luisospina.inventario.application.usecase.UseCaseWithFluxReturn;
import com.luisospina.inventario.domain.movement.MovementDomain;

public interface ListMovementsByProductUseCase extends UseCaseWithFluxReturn<Long, MovementDomain> {
}
