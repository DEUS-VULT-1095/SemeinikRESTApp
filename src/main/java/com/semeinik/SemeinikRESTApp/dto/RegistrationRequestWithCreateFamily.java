package com.semeinik.SemeinikRESTApp.dto;

import jakarta.validation.Valid;

/**
 * Класс для запроса на регистрацию и создание семьи.
 *
 * Этот класс представляет собой запрос на регистрацию пользователя и создание новой семьи.
 * Он содержит информацию о пользователе ({@link PersonDTO}) и информацию о семье ({@link FamilyDTO}).
 * Класс также предоставляет методы доступа для чтения и установки значений этих полей.
 * @Valid Эта аннотация на полях указывает, что поля внутри этого объекта должны быть провалидированы.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class RegistrationRequestWithCreateFamily {
    @Valid
    private PersonDTO personDTO; // Информация о пользователе.
    @Valid
    private FamilyDTO familyDTO; // Информация о семье.

    public PersonDTO getPersonDTO() {
        return personDTO;
    }

    public void setPersonDTO(PersonDTO personDTO) {
        this.personDTO = personDTO;
    }

    public FamilyDTO getFamilyDTO() {
        return familyDTO;
    }

    public void setFamilyDTO(FamilyDTO familyDTO) {
        this.familyDTO = familyDTO;
    }

    public String getEmail() {
        return personDTO.getEmail();
    }
}
