package com.semeinik.SemeinikRESTApp.exceptions;

import com.semeinik.SemeinikRESTApp.models.Family;

/**
 * Исключение, выбрасываемое если семья ({@link Family}) не была создана и сохранена в БД.
 *
 * Это исключение представляет собой подкласс RuntimeException.
 * Исключение содержит сообщение об ошибке, которое может быть передано пользователю.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class FamilyNotCreatedException extends RuntimeException {
    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public FamilyNotCreatedException(String message) {
        super(message);
    }
}
