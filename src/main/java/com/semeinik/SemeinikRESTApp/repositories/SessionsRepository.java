package com.semeinik.SemeinikRESTApp.repositories;

import com.semeinik.SemeinikRESTApp.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.semeinik.SemeinikRESTApp.models.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс репозитория для управления данными сессий ({@link Session}).
 *
 * Этот интерфейс предоставляет методы для поиска и управления данными сессий в базе данных.
 * Он расширяет JpaRepository, предоставляя стандартные операции CRUD (создание, чтение, обновление, удаление)
 * для сущности {@link Session}.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Repository
public interface SessionsRepository extends JpaRepository<Session, Integer> {

    /**
     * Метод для поиска всех сессий ({@link Session}), связанных с определенным пользователем ({@link Person}) по его идентификатору (id).
     *
     * @param personId Идентификатор пользователя ({@link Person}) для поиска связанных с ним сессий ({@link Session}).
     * @return Список сессий ({@link Session}), связанных с указанным пользователем ({@link Person}).
     */
    List<Session> findAllByPersonId(int personId);

    /**
     * Метод для удаления всех сессий ({@link Session}), связанных с определенным пользователем ({@link Person}) по его идентификатору (id).
     *
     * @param personId Идентификатор пользователя ({@link Person}), сессии ({@link Session}) которого требуется удалить.
     */
    void deleteAllByPersonId(int personId);

    /**
     * Метод для поиска сессии ({@link Session}) по значению токена обновления (refresh token).
     *
     * @param refreshToken Значение токена обновления для поиска сессии ({@link Session}).
     * @return Опциональный объект, содержащий найденную сессию ({@link Session}), если она существует.
     */
    Optional<Session> findByRefreshToken(UUID refreshToken);

    /**
     * Метод для удаления сессии ({@link Session}) по значению токена обновления (refresh token).
     *
     * @param refreshToken Значение токена обновления для удаления сессии ({@link Session}).
     */
    void deleteByRefreshToken(UUID refreshToken);
}
