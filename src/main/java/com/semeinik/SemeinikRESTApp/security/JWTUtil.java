package com.semeinik.SemeinikRESTApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.semeinik.SemeinikRESTApp.models.Family;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.semeinik.SemeinikRESTApp.models.Person;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

/**
 * Компонент для генерации и валидации JWT-токенов.
 *
 * Этот компонент предоставляет методы для создания JWT-токенов на основе указанных данных и для проверки
 * и извлечения данных из существующего JWT-токена. Кроме того, он использует секретный ключ (secret),
 * предоставленный в конфигурации, для подписи и верификации токенов.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Component
public class JWTUtil {
    /** Секрет для создания подписи. */
    @Value("${jwtSecret}")
    private String secret; // Секретный ключ, хранящийся в application.properties

    /**
     * Метод для генерации JWT-токена на основе переданных данных.
     *
     * @param id     Идентификатор пользователя ({@link Person}).
     * @param email  Электронная почта пользователя.
     * @param role   Роль пользователя.
     * @param family Семья ({@link Family}), к которой принадлежит пользователь ({@link Person}) (может быть null).
     * @return Сгенерированный JWT-токен.
     */
    public String generateToken(int id, String email, String role, Family family) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(30).toInstant());

        return JWT.create()
                .withSubject("Person details")
                .withClaim("id", id)
                .withClaim("email", email)
                .withClaim("role", role)
                .withClaim("familyId", (family == null ? null : family.getId()))
                .withIssuedAt(new Date()) // Когда создан.
                .withIssuer("Semeinik") // Кем создан.
                .withExpiresAt(expirationDate) // Когда истекает срок действия.
                .sign(Algorithm.HMAC256(secret)); // Подпись с помощью секрета (secret).
    }

    /**
     * Метод для валидации JWT-токена и извлечения его данных.
     *
     * @param token JWT-токен, который требуется валидировать и извлечь данные.
     * @return Map данных (Claims), извлеченных из JWT-токена.
     * @throws JWTVerificationException если JWT-токен не прошел верификацию, будет сгенерировано исключение и
     * проброшено дальше.
     */
    public Map<String, Claim> validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("Person details")
                .withIssuer("Semeinik")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaims();
    }
}
