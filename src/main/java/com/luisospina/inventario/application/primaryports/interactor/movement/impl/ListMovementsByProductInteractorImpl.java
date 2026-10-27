package com.luisospina.inventario.application.primaryports.interactor.movement.impl;

import com.luisospina.inventario.application.primaryports.dto.movement.response.ListMovementResponse;
import com.luisospina.inventario.application.primaryports.interactor.movement.ListMovementsByProductInteractor;
import com.luisospina.inventario.application.primaryports.mapper.movement.MovementDTOMapper;
import com.luisospina.inventario.application.usecase.movement.ListMovementsByProductUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ListMovementsByProductInteractorImpl implements ListMovementsByProductInteractor {

    private final ListMovementsByProductUseCase listMovementsByProductUseCase;
    private final MovementDTOMapper movementDTOMapper;

    public ListMovementsByProductInteractorImpl(
            ListMovementsByProductUseCase listMovementsByProductUseCase, MovementDTOMapper movementDTOMapper) {
        this.listMovementsByProductUseCase = listMovementsByProductUseCase;
        this.movementDTOMapper = movementDTOMapper;
    }

    @Override
    public Flux<ListMovementResponse> execute(Long productId) {
        return listMovementsByProductUseCase.execute(productId).map(movementDTOMapper::toListMovementResponse);
    }
}
