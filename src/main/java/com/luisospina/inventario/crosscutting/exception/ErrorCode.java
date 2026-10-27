package com.luisospina.inventario.crosscutting.exception;

/** Contrato de los enums de error de cada módulo ({@code ProductError}, {@code MovementError}...). */
public interface ErrorCode {

    String getCode();

    String getMessageTemplate();
}
