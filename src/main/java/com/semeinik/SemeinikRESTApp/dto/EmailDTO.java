package com.semeinik.SemeinikRESTApp.dto;

import jakarta.validation.constraints.Email;

/**
 * Data Transfer Object (DTO) для электронной почты.
 *
 * Этот класс представляет собой объект передачи данных (DTO), используемый для передачи электронной почты.
 * Он содержит поле для адреса электронной почты пользователя и включает валидацию формата электронной почты.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class EmailDTO {
    /**
     * Адрес электронной почты пользователя.
     *
     * Данное поле используется для хранения адреса электронной почты пользователя и подвергается валидации,
     * чтобы проверить его соответствие формату адреса электронной почты.
     */
    @Email(message = "It's not email format")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
