package com.luisospina.inventario.crosscutting.helpers;

public final class ObjectHelper {

    private ObjectHelper() {
    }

    public static <T> T getDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
