package com.luisospina.inventario.domain;

/** Base de las entidades de dominio. El id es nulo hasta que la entidad se persiste. */
public abstract class BaseDomain {

    private final Long id;

    protected BaseDomain(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
