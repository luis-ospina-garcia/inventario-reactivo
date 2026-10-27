package com.luisospina.inventario.infrastructure.primaryadapters.controller.movement;

import com.luisospina.inventario.application.primaryports.dto.movement.request.CreateMovementRequest;
import com.luisospina.inventario.application.primaryports.dto.movement.response.CreateMovementResponse;
import com.luisospina.inventario.application.primaryports.dto.movement.response.ListMovementResponse;
import com.luisospina.inventario.application.primaryports.interactor.movement.CreateMovementInteractor;
import com.luisospina.inventario.application.primaryports.interactor.movement.ListMovementsByProductInteractor;
import com.luisospina.inventario.infrastructure.ResponseMessages;
import com.luisospina.inventario.infrastructure.primaryadapters.adapter.response.ApiResponse;
import com.luisospina.inventario.infrastructure.primaryadapters.adapter.response.HttpResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movements")
public class MovementController {

    private final CreateMovementInteractor createMovementInteractor;
    private final ListMovementsByProductInteractor listMovementsByProductInteractor;

    public MovementController(
            CreateMovementInteractor createMovementInteractor,
            ListMovementsByProductInteractor listMovementsByProductInteractor) {
        this.createMovementInteractor = createMovementInteractor;
        this.listMovementsByProductInteractor = listMovementsByProductInteractor;
    }

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<CreateMovementResponse>>> createMovement(
            @RequestBody @Valid CreateMovementRequest request) {
        return HttpResponses.created(createMovementInteractor.execute(request), ResponseMessages.MOVEMENT_CREATED);
    }

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<ListMovementResponse>>> listMovements(@RequestParam Long productId) {
        return HttpResponses.okList(listMovementsByProductInteractor.execute(productId));
    }
}
