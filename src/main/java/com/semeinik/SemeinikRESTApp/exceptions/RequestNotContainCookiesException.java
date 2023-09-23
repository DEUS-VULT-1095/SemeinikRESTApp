package com.semeinik.SemeinikRESTApp.exceptions;

/**
 * Исключение, выбрасываемое если запрос не содержит куки.
 *
 * Это исключение представляет собой подкласс RuntimeException.
 * Исключение содержит сообщение об ошибке, которое может быть передано пользователю.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class RequestNotContainCookiesException extends RuntimeException {
    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public RequestNotContainCookiesException(String message) {
        super(message);
    }
}
