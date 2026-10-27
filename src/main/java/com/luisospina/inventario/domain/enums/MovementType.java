package com.luisospina.inventario.domain.enums;

public enum MovementType {
    IN,
    OUT;

    /** Variación de stock que produce este tipo de movimiento para una cantidad dada. */
    public int delta(int quantity) {
        return this == IN ? quantity : -quantity;
    }
}
