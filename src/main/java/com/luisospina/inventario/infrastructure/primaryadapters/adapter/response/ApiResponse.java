package com.luisospina.inventario.infrastructure.primaryadapters.adapter.response;

import java.util.List;

public class ApiResponse<T> extends Response<T> {

    public static <T> ApiResponse<T> of(T item) {
        ApiResponse<T> response = new ApiResponse<>();
        if (item != null) {
            response.getData().add(item);
        }
        return response;
    }

    public static <T> ApiResponse<T> of(T item, String message) {
        ApiResponse<T> response = of(item);
        if (message != null && !message.isBlank()) {
            response.getMessages().add(message);
        }
        return response;
    }

    public static <T> ApiResponse<T> of(List<T> items) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(items);
        return response;
    }
}
