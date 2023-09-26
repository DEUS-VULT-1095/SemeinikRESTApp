package com.semeinik.SemeinikRESTApp.dto;

/**
 * Data Transfer Object (DTO) для аутентификации пользователя.
 *
 * Этот класс представляет собой объект передачи данных (DTO), используемый для передачи данных при аутентификации
 * пользователя. Он содержит поля для электронной почты и пароля пользователя.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class AuthDTO {
    private String email; // Электронная почта пользователя.
    private String password; // Пароль пользователя.

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
