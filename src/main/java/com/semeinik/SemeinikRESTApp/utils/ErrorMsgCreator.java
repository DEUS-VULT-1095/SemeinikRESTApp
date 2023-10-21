package com.semeinik.SemeinikRESTApp.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Класс для создания текстового сообщения об ошибках, на основе данных из BindingResult.
 * Этот класс предоставляет статический метод для создания текстового сообщения, содержащего
 * информацию о полях, в которых возникли ошибки, и соответствующих сообщениях об ошибках.
 */
public final class ErrorMsgCreator {

    private ErrorMsgCreator() {
    }

    /**
     * Создает текстовое сообщение об ошибках на основе данных из BindingResult.
     *
     * @param bindingResult Объект BindingResult, содержащий информацию о проверке ограничений (validation).
     * @return Текстовое сообщение, содержащее информацию о полях с ошибками и сообщениях об ошибках.
     */
    public static String createErrorMsg(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(f -> sb.append(f.getField() + " - " + f.getDefaultMessage() + "; "));
        return sb.toString();
    }
}
