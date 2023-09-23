package com.semeinik.SemeinikRESTApp.repositories;

import com.semeinik.SemeinikRESTApp.models.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс репозитория для управления данными сущности {@link ActivationToken}.
 *
 * Этот интерфейс предоставляет методы для поиска и управления активационными токенами в базе данных.
 * Он расширяет JpaRepository, предоставляя стандартные операции CRUD (создание, чтение, обновление, удаление)
 * для сущности {@link ActivationToken}.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Repository
public interface ActivationTokensRepository extends JpaRepository<ActivationToken, Integer> {

    /**
     * Метод для поиска активационного токена по его значению (UUID).
     *
     * @param token Значение активационного токена (UUID).
     * @return Опциональный объект, содержащий найденный активационный токен, если он существует.
     */
    Optional<ActivationToken> findByToken(UUID token);
}
