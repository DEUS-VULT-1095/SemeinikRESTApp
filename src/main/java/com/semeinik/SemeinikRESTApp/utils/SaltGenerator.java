package com.semeinik.SemeinikRESTApp.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Утилитарный класс для генерации соли (Salt).
 * Этот класс предоставляет статический метод для генерации случайной соли
 * в виде строки, кодированной в формате Base64.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class SaltGenerator {

    /**
     * Генерирует случайную соль (Salt) и возвращает ее в виде строки, закодированной в Base64.
     *
     * @return Сгенерированная соль в виде строки в формате Base64.
     */
    public static String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[32];
        secureRandom.nextBytes(salt);
        String saltString = Base64.getEncoder().encodeToString(salt);

        return saltString;
    }
}
