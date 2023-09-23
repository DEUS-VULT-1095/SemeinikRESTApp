package com.semeinik.SemeinikRESTApp.dto;

import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.models.Family;

/**
 * Этот класс представляет собой объект ответа
 * на регистрацию пользователя ({@link Person}) с возможностью создания семьи ({@link Family}). Он хранит идентификатор семьи,
 * связанный с пользователем после успешной регистрации.
 */
public class RegistrationResponseWithCreateFamily {
    private String familyIdentifier;

    /**
     * Конструктор создает новый объект {@code RegistrationResponseWithCreateFamily} с указанным
     * идентификатором семьи.
     *
     * @param familyIdentifier Идентификатор, который будет связан с семьей пользователя.
     */
    public RegistrationResponseWithCreateFamily(String familyIdentifier) {
        this.familyIdentifier = familyIdentifier;
    }

    public String getFamilyIdentifier() {
        return familyIdentifier;
    }

    public void setFamilyIdentifier(String familyIdentifier) {
        this.familyIdentifier = familyIdentifier;
    }
}
