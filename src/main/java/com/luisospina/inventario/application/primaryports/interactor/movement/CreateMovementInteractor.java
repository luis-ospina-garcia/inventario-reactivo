package com.luisospina.inventario.application.primaryports.interactor.movement;

import com.luisospina.inventario.application.primaryports.dto.movement.request.CreateMovementRequest;
import com.luisospina.inventario.application.primaryports.dto.movement.response.CreateMovementResponse;
import com.luisospina.inventario.application.primaryports.interactor.InteractorWithReturn;

public interface CreateMovementInteractor
        extends InteractorWithReturn<CreateMovementRequest, CreateMovementResponse> {
}
