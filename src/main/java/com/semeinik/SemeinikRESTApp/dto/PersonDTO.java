package com.semeinik.SemeinikRESTApp.dto;

import com.semeinik.SemeinikRESTApp.models.FamilyRole;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) для информации о пользователе.
 *
 * Этот класс представляет собой объект передачи данных (DTO), используемый для передачи информации о пользователе.
 * Он содержит поля для адреса электронной почты, имени, даты рождения, роли в семье, пароля и подтверждения пароля,
 * а также методы доступа для чтения и установки значений этих полей.
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class PersonDTO {
    @Pattern( regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Поле должно соответсвовать email формату")
    private String email; // Адрес электронной почты пользователя.
//    @NotEmpty(message = "Имя не должно быть пустым.")
//    @NotNull(message = "Имя не должно быть пустым.")
//    @Pattern(regexp = "^[A-Za-zА-Яа-я-]{3,30}$", message = "Имя должно состоять из латинских или русских букв." +
//            " Не содержать пробелы. Может содержать символ дефиса \"-\". От 3-х до 30-ти символов включительно.")
    private String name; // Имя пользователя.
    private LocalDate dateOfBirth; // Дата рождения пользователя.
    private FamilyRole familyRole; // Роль пользователя в семье.
    @NotEmpty(message = "Пароль не может быть пустым.")
    @NotNull(message = "Пароль не может быть пустым.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,16}$", message = "Пароль должен содержать от 8 до 16 " +
            "символов, включающих в себя латинские буквы верхнего (A...Z) и нижнего (a...z) регистров, а также цифры")
    private String password; // Пароль пользователя.
//    private String confirmPassword; // Подтверждение пароля пользователя.

    public PersonDTO() {
    }

    public PersonDTO(String email, String name, LocalDate dateOfBirth, FamilyRole familyRole, String password) {
        this.email = email;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.familyRole = familyRole;
        this.password = password;
        //this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public FamilyRole getFamilyRole() {
        return familyRole;
    }

    public void setFamilyRole(FamilyRole familyRole) {
        this.familyRole = familyRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getConfirmPassword() {
//        return confirmPassword;
//    }
//
//    public void setConfirmPassword(String confirmPassword) {
//        this.confirmPassword = confirmPassword;
//    }
}
