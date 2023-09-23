package com.semeinik.SemeinikRESTApp.dto;

/**
 * Data Transfer Object (DTO) для идентификатора семьи.
 *
 * Этот класс представляет собой объект передачи данных (DTO), используемый для передачи идентификатора семьи.
 * Он содержит поле для идентификатора семьи и методы доступа для чтения и установки значения этого поля.
 * @author Denis Kolesnikov
 * @version 1.0
 *
 */
public class FamilyIdentifierDTO {
    String familyIdentifier; // Идентификатор семьи.

    public String getFamilyIdentifier() {
        return familyIdentifier;
    }

    public void setFamilyIdentifier(String familyIdentifier) {
        this.familyIdentifier = familyIdentifier;
    }
}
