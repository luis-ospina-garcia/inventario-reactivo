package com.luisospina.inventario.application.primaryports.interactor.movement;

import com.luisospina.inventario.application.primaryports.dto.movement.response.ListMovementResponse;
import com.luisospina.inventario.application.primaryports.interactor.InteractorWithFluxReturn;

public interface ListMovementsByProductInteractor extends InteractorWithFluxReturn<Long, ListMovementResponse> {
}
