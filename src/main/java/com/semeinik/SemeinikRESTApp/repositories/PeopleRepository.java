package com.semeinik.SemeinikRESTApp.repositories;

import com.semeinik.SemeinikRESTApp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс репозитория для управления данными сущности {@link Person}.
 *
 * Этот интерфейс предоставляет методы для поиска и управления данными о пользователях в базе данных.
 * Он расширяет JpaRepository, предоставляя стандартные операции CRUD (создание, чтение, обновление, удаление)
 * для сущности {@link Person}.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    /**
     * Метод для поиска пользователя ({@link Person}) по его адресу электронной почты.
     *
     * @param email Адрес электронной почты для поиска пользователя.
     * @return Опциональный объект, содержащий найденного пользователя, если он существует.
     */
    Optional<Person> findByEmail(String email);

    /**
     * Метод для поиска пользователя ({@link Person}) по соли (salt).
     *
     * @param salt Соль для поиска пользователя ({@link Person}).
     * @return Опциональный объект, содержащий найденного пользователя, если он существует.
     */
    Optional<Person> findBySalt(String salt);
}
