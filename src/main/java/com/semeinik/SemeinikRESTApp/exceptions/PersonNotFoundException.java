package com.semeinik.SemeinikRESTApp.exceptions;

import com.semeinik.SemeinikRESTApp.models.Person;

/**
 * Исключение, выбрасываемое если пользователь ({@link Person}) не был найден в БД.
 *
 * Это исключение представляет собой подкласс RuntimeException.
 * Исключение содержит сообщение об ошибке, которое может быть передано пользователю.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class PersonNotFoundException extends RuntimeException {
    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public PersonNotFoundException(String message) {
        super(message);
    }
}
