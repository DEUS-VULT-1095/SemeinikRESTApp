package com.semeinik.SemeinikRESTApp.exceptions;

/**
 * Исключение, выбрасываемое при неудачной активации аккаунта.
 *
 * Это исключение представляет собой подкласс RuntimeException.
 * Исключение содержит сообщение об ошибке, которое может быть передано пользователю.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class ActivationAccountException extends RuntimeException {
    /**
     * Конструктор исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public ActivationAccountException(String message) {
        super(message);
    }
}
