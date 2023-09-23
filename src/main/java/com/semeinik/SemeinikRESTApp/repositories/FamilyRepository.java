package com.semeinik.SemeinikRESTApp.repositories;

import com.semeinik.SemeinikRESTApp.models.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для управления данными сущности {@link Family}.
 *
 * Этот интерфейс предоставляет методы для поиска и управления семейными данными в базе данных.
 * Он расширяет JpaRepository, предоставляя стандартные операции CRUD (создание, чтение, обновление, удаление)
 * для сущности {@link Family}.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Repository
public interface FamilyRepository extends JpaRepository<Family, Integer> {

    /**
     * Метод для поиска семьи по ее идентификатору.
     *
     * @param familyIdentifier Уникальный идентификатор семьи.
     * @return Опциональный объект, содержащий найденную семью, если она существует.
     */
    Optional<Family> findByFamilyIdentifier(String familyIdentifier);
}
