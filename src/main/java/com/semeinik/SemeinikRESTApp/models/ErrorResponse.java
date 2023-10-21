package com.semeinik.SemeinikRESTApp.models;

import java.time.LocalDateTime;

/**
 * Класс, представляющий объект ответа об ошибке (Error Response).
 * Этот класс содержит информацию об ошибке, включая текстовое сообщение и метку времени,
 * когда ошибка произошла.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class ErrorResponse {
    private String message;
    private LocalDateTime localDateTime;

    /**
     * Конструктор для создания объекта ErrorResponse с указанным текстовым сообщением и меткой времени.
     *
     * @param message       Текстовое сообщение об ошибке.
     * @param localDateTime Метка времени, когда ошибка произошла.
     */
    public ErrorResponse(String message, LocalDateTime localDateTime) {
        this.message = message;
        this.localDateTime = localDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
