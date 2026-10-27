package com.luisospina.inventario.infrastructure;

import com.luisospina.inventario.crosscutting.exception.DomainException;
import com.luisospina.inventario.domain.product.exceptions.DuplicateSkuException;
import com.luisospina.inventario.domain.product.exceptions.InsufficientStockException;
import com.luisospina.inventario.domain.product.exceptions.ProductNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/** Único punto donde una excepción de negocio se traduce a un estado HTTP. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFound(DomainException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(toError(ex)));
    }

    @ExceptionHandler({DuplicateSkuException.class, InsufficientStockException.class})
    public Mono<ResponseEntity<ErrorResponse>> handleConflict(DomainException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(toError(ex)));
    }

    /** Resto de errores de dominio (datos inválidos): 400. */
    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDomain(DomainException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(toError(ex)));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidation(WebExchangeBindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> messages = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        String message = messages.isEmpty() ? "Solicitud inválida" : messages.getFirst();
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of("VALIDATION_ERROR", message, messages)));
    }

    /** Errores del propio framework (JSON mal formado, ruta inexistente...): se respeta su estado. */
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleResponseStatus(ResponseStatusException ex) {
        String message = ex.getReason() != null ? ex.getReason() : ex.getStatusCode().toString();
        return Mono.just(ResponseEntity.status(ex.getStatusCode())
                .body(ErrorResponse.of("HTTP_" + ex.getStatusCode().value(), message)));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUnexpected(Exception ex) {
        log.error("Error no controlado", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("INTERNAL_ERROR", "Ocurrió un error inesperado")));
    }

    private static ErrorResponse toError(DomainException ex) {
        return ErrorResponse.of(ex.getCode(), ex.getMessage());
    }
}
