package com.semeinik.SemeinikRESTApp.dto;

import jakarta.validation.Valid;

/**
 * Класс для запроса на регистрацию и присоединение к семье.
 *
 * Этот класс представляет собой запрос на регистрацию пользователя и присоединение к семье.
 * Он содержит информацию о пользователе ({@link PersonDTO}) и идентификаторе семьи ({@link FamilyIdentifierDTO}).
 * Класс также предоставляет методы доступа для чтения и установки значений этих полей.
 * @Valid Эта аннотация на полях указывает, что поля внутри этого объекта должны быть провалидированы.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class RegistrationRequestJoinFamily {
    @Valid
    private PersonDTO personDTO; // Информация о пользователе.
    private FamilyIdentifierDTO familyIdentifierDTO; // Идентификатор семьи.

    public PersonDTO getPersonDTO() {
        return personDTO;
    }

    public void setPersonDTO(PersonDTO personDTO) {
        this.personDTO = personDTO;
    }

    public FamilyIdentifierDTO getFamilyIdentifierDTO() {
        return familyIdentifierDTO;
    }

    public void setFamilyIdentifierDTO(FamilyIdentifierDTO familyIdentifierDTO) {
        this.familyIdentifierDTO = familyIdentifierDTO;
    }

    public String getEmail() {
        return personDTO.getEmail();
    }
}
