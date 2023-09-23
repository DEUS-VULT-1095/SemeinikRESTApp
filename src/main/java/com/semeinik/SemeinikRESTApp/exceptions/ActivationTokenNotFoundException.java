package com.semeinik.SemeinikRESTApp.exceptions;

import com.semeinik.SemeinikRESTApp.models.ActivationToken;

/**
 * Исключение, выбрасываемое если активационный токен ({@link ActivationToken}) не был найден в БД.
 *
 * Это исключение представляет собой подкласс RuntimeException.
 * Исключение содержит сообщение об ошибке, которое может быть передано пользователю.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class ActivationTokenNotFoundException extends RuntimeException {
    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public ActivationTokenNotFoundException(String message) {
        super(message);
    }
}
