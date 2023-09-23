package com.semeinik.SemeinikRESTApp.exceptions;

/**
 * Исключение, выбрасываемое если был указан не валидный email.
 *
 * Это исключение представляет собой подкласс RuntimeException.
 * Исключение содержит сообщение об ошибке, которое может быть передано пользователю.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class InvalidEmailException extends RuntimeException {
    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public InvalidEmailException(String message) {
        super(message);
    }
}
