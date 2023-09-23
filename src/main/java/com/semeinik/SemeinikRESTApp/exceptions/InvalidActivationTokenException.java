package com.semeinik.SemeinikRESTApp.exceptions;

import com.semeinik.SemeinikRESTApp.models.ActivationToken;

/**
 * Исключение, выбрасываемое если объект {@link ActivationToken} не валиден.
 *
 * Это исключение представляет собой подкласс RuntimeException.
 * Исключение содержит сообщение об ошибке, которое может быть передано пользователю.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class InvalidActivationTokenException extends RuntimeException {
    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public InvalidActivationTokenException(String message) {
        super(message);
    }
}
