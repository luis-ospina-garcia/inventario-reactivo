package com.luisospina.inventario.infrastructure.primaryadapters.adapter.response;

import java.util.ArrayList;
import java.util.List;

/** Envoltorio común de las respuestas de éxito: los datos y los mensajes para el usuario. */
public abstract class Response<T> {

    private List<T> data = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data != null ? data : new ArrayList<>();
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages != null ? messages : new ArrayList<>();
    }
}
