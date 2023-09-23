package com.semeinik.SemeinikRESTApp.exceptions;

import com.semeinik.SemeinikRESTApp.models.Family;

/**
 * Исключение, выбрасываемое если объект {@link Family} не найден в БД.
 *
 * Это исключение представляет собой подкласс RuntimeException.
 * Исключение содержит сообщение об ошибке, которое может быть передано пользователю.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class FamilyNotFoundException extends RuntimeException {
    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public FamilyNotFoundException(String message) {
        super(message);
    }
}
