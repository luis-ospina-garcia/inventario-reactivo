package com.luisospina.inventario.crosscutting.exception;

import java.io.Serial;

/**
 * Base de todas las excepciones de negocio. El mensaje sale de la plantilla del {@link ErrorCode}
 * formateada con los argumentos, así el texto vive en un solo lugar (el enum del módulo).
 */
public abstract class DomainException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;
    private final Layer layer;
    private final transient Object[] args;

    protected DomainException(ErrorCode errorCode, Layer layer, Object... args) {
        super(String.format(errorCode.getMessageTemplate(), args));
        this.errorCode = errorCode;
        this.layer = layer;
        this.args = args;
    }

    protected DomainException(ErrorCode errorCode, Object... args) {
        this(errorCode, Layer.DOMAIN, args);
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Layer getLayer() {
        return layer;
    }

    public Object[] getArgs() {
        return args;
    }
}
