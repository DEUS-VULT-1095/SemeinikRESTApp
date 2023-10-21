package com.semeinik.SemeinikRESTApp.utils;

import com.semeinik.SemeinikRESTApp.dto.PersonDTO;
import com.semeinik.SemeinikRESTApp.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Класс, реализующий интерфейс {@link Validator} для проверки объектов типа {@link PersonDTO}.
 * Этот валидатор осуществляет проверку данных в объекте PersonDTO, в частности,
 * сравнивает пароль и подтверждение пароля, а также проверяет уникальность адреса электронной почты (email).
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Component
public final class PersonDTOValidator implements Validator {
    private final PeopleService peopleService;

    /**
     * Конструктор класса PersonDTOValidator.
     *
     * @param peopleService Сервис для работы с данными о людях ({@link PeopleService}).
     */
    @Autowired
    private PersonDTOValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    /**
     * Проверяет, поддерживает ли валидатор проверку объекта указанного класса ({@link PersonDTO}).
     *
     * @param clazz Класс объекта для проверки.
     * @return true, если валидатор поддерживает проверку данного класса; в противном случае false.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDTO.class.equals(clazz);
    }

    /**
     * Выполняет валидацию объекта {@link PersonDTO} и добавляет ошибки в объект {@link Errors} при несоответствии.
     *
     * @param target Объект для валидации ({@link PersonDTO}).
     * @param errors Объект для добавления ошибок при их обнаружении ({@link Errors}).
     */
    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO personDTO = (PersonDTO) target;

        // Проверка на совпадение пароля и подтверждения пароля
        if (!personDTO.getPassword().equals(personDTO.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "", "Confirm password is not correct");
        }

        // Проверка уникальности адреса электронной почты (email) среди пользователей
        if (peopleService.findByEmail(personDTO.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This username already exist");
        }
    }
}
