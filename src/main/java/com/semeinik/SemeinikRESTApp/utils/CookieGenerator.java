package com.semeinik.SemeinikRESTApp.utils;

import jakarta.servlet.http.Cookie;

import java.util.UUID;


/**
 * Утилитарный класс для генерации куки ({@link Cookie}) с Refresh Token.
 * Этот класс предоставляет статический метод для генерации нового куки, предназначенного для хранения Refresh Token.
 * Куки устанавливаются с именем "refreshToken", содержащим значением Refresh Token в формате UUID,
 * а также имеют определенный срок действия, путь и ограничения безопасности.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class CookieGenerator {

    /**
     * Генерирует новый куки ({@link Cookie}) для хранения Refresh Token.
     *
     * @return Сгенерированный куки ({@link Cookie}) с именем "refreshToken" и содержащий
     *         значение Refresh Token в виде UUID.
     */
    public static Cookie generateCookie() {
        UUID refreshToken = UUID.randomUUID();

        Cookie cookie = new Cookie("refreshToken", refreshToken.toString());
        int expirationSeconds = 15_552_000; // срок действия в секундах (180 дней)
        cookie.setMaxAge(expirationSeconds);
        cookie.setPath("/auth"); // Устанавливает путь, на котором действителен куки
        cookie.setHttpOnly(true); // Ограничивает доступ JavaScript к куки для повышения безопасности

        return cookie;
    }
}
