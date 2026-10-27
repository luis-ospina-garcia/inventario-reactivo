package com.luisospina.inventario.application.primaryports.interactor.movement.impl;

import com.luisospina.inventario.application.primaryports.dto.movement.request.CreateMovementRequest;
import com.luisospina.inventario.application.primaryports.dto.movement.response.CreateMovementResponse;
import com.luisospina.inventario.application.primaryports.interactor.movement.CreateMovementInteractor;
import com.luisospina.inventario.application.primaryports.mapper.movement.MovementDTOMapper;
import com.luisospina.inventario.application.usecase.movement.CreateMovementUseCase;
import com.luisospina.inventario.application.usecase.stock.PublishStockUpdatedUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateMovementInteractorImpl implements CreateMovementInteractor {

    private final CreateMovementUseCase createMovementUseCase;
    private final PublishStockUpdatedUseCase publishStockUpdatedUseCase;
    private final MovementDTOMapper movementDTOMapper;

    public CreateMovementInteractorImpl(
            CreateMovementUseCase createMovementUseCase,
            PublishStockUpdatedUseCase publishStockUpdatedUseCase,
            MovementDTOMapper movementDTOMapper) {
        this.createMovementUseCase = createMovementUseCase;
        this.publishStockUpdatedUseCase = publishStockUpdatedUseCase;
        this.movementDTOMapper = movementDTOMapper;
    }

    @Override
    public Mono<CreateMovementResponse> execute(CreateMovementRequest request) {
        // La transacción del caso de uso se confirma cuando su Mono termina; solo entonces se
        // publica el evento, así ningún suscriptor ve un cambio de stock que luego se revierta.
        return createMovementUseCase.execute(movementDTOMapper.toCommand(request))
                .flatMap(result -> publishStockUpdatedUseCase.execute(result).thenReturn(result))
                .map(movementDTOMapper::toCreateMovementResponse);
    }
}
