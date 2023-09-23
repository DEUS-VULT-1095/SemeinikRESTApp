package com.semeinik.SemeinikRESTApp.controllers;

import com.semeinik.SemeinikRESTApp.exceptions.ActivationTokenNotFoundException;
import com.semeinik.SemeinikRESTApp.exceptions.ActivationAccountException;
import com.semeinik.SemeinikRESTApp.exceptions.InvalidActivationTokenException;
import com.semeinik.SemeinikRESTApp.exceptions.PersonNotFoundException;
import com.semeinik.SemeinikRESTApp.models.ActivationToken;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.services.AccountActivationService;
import com.semeinik.SemeinikRESTApp.services.ActivationTokensService;
import com.semeinik.SemeinikRESTApp.services.EmailService;
import com.semeinik.SemeinikRESTApp.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Контроллер для активации аккаунта и отправки ссылки для активации.
 * Он предоставляет эндпоинты для активации аккаунта с использованием активационного токена
 * и для отправки ссылки активации на зарегистрированного email.
 * Этот класс использует аннотацию {@code @RestController}, чтобы указать, что он является контроллером Spring MVC.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@RestController
public class AccountActivationController {
    private final AccountActivationService accountActivationService;

    /**
     * Конструктор класса {@code AccountActivationController} с инъекцией зависимости {@link AccountActivationService}.
     *
     * @param accountActivationService    Сервис для активации учетных записей и отправки активационных ссылок ({@link AccountActivationService}).
     */
    @Autowired
    public AccountActivationController(AccountActivationService accountActivationService) {
        this.accountActivationService = accountActivationService;
    }

    /**
     * Метод {@code activateAccount} обрабатывает GET-запрос для активации учетной записи пользователя
     * с использованием активационного токена.
     *
     * @param activationToken    Активационный токен для активации учетной записи.
     * @return                   Ответ, указывающий на успешную активацию учетной записи.
     */
    @GetMapping("/activate/{activationToken}")
    public ResponseEntity<Object> activateAccount(@PathVariable String activationToken) {
        accountActivationService.activateAccount(activationToken);

        return ResponseEntity.ok().build();
    }

    /**
     * Метод обрабатывает POST-запрос для отправки активационной ссылки
     * на электронную почту пользователя.
     *
     * @return    Ответ, указывающий на успешную отправку активационной ссылки.
     */
    @PostMapping("/send-activation-link")
    public ResponseEntity<Object> sendActivationLink() {
        accountActivationService.sendActivationLink();

        return ResponseEntity.ok().build();
    }
}
