package com.semeinik.SemeinikRESTApp.utils;

import com.semeinik.SemeinikRESTApp.models.ActivationToken;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.services.ActivationTokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Утилитный класс для генерации активационного токена.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
public final class ActivationTokenGenerator {

    private ActivationTokenGenerator() {
    }

    /**
     * Генерирует активационный токен для пользователя.
     *
     * @return Вовзращает сгенерированный активационный токен.
     */
    public static UUID generateActivationToken() {
        return UUID.randomUUID();
    }
}
