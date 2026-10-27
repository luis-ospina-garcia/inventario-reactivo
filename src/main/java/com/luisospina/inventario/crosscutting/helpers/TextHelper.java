package com.luisospina.inventario.crosscutting.helpers;

public final class TextHelper {

    private TextHelper() {
    }

    /** Recorta espacios; un valor nulo se convierte en cadena vacía. */
    public static String applyTrim(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
