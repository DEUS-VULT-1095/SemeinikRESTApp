package com.semeinik.SemeinikRESTApp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object (DTO) для семейной фамилии.
 *
 * Этот класс представляет собой объект передачи данных (DTO), используемый для передачи семейной фамилии.
 * Он содержит поле для фамилии и включает валидацию, чтобы проверить, что фамилия соответствует определенным критериям.
 * @author Denis Kolesnikov
 * @version 1.0
 *
 */
public class FamilyDTO {
    /**
     * Фамилия семьи.
     *
     * Данное поле используется для хранения семейной фамилии и подвергается валидации на пустое значение и формат
     * фамилии с использованием регулярного выражения.
     */
    @NotNull(message = "Фамилия не должна быть пустой.")
    @NotEmpty(message = "Фамилия не должна быть пустой.")
    @Pattern(regexp = "^[A-Za-zА-Яа-я ]{3,30}$", message = "Фамилия должна состоять из латинских или русских букв." +
            " От 3-х до 30-ти символов включительно.")
    private String familyName;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
