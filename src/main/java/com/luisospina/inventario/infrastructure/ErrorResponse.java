package com.luisospina.inventario.infrastructure;

import java.util.List;

/** Cuerpo de toda respuesta de error: código estable, mensaje legible y, si aplica, el detalle por campo. */
public record ErrorResponse(String code, String message, List<String> messages) {

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, List.of(message));
    }

    public static ErrorResponse of(String code, String message, List<String> messages) {
        return new ErrorResponse(code, message, messages);
    }
}
