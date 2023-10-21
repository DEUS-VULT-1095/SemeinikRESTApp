package com.semeinik.SemeinikRESTApp.utils;

import java.security.SecureRandom;

/**
 * Утилитарный класс для генерации уникального идентификатора семьи (Family Identifier).
 * Этот класс предоставляет статический метод для создания случайной строки,
 * которая может быть использована в качестве уникального идентификатора семьи.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public final class IdentifierFamilyGenerator {
    /** Символы для генерации идентификатора. */
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private IdentifierFamilyGenerator() {
    }

    /**
     * Генерирует уникальный идентификатор семьи (Family Identifier).
     *
     * @return Сгенерированная случайная строка, которая может быть использована
     *         в качестве уникального идентификатора семьи.
     */
    public static String generateFamilyIdentifier() {
        SecureRandom secureRandom = new SecureRandom();

        StringBuilder stringBuilder = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(index));
        }

        return stringBuilder.toString();
    }
}
