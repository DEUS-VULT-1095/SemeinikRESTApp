package com.semeinik.SemeinikRESTApp.controllers;

import com.semeinik.SemeinikRESTApp.services.AccountActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
@RequestMapping("/activate")
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
     */
    @GetMapping("/activation-token/{activationToken}")
    @ResponseStatus(HttpStatus.OK)
    public void activateAccount(@PathVariable String activationToken) {
        accountActivationService.activateAccount(activationToken);
    }

    /**
     * Метод обрабатывает POST-запрос для отправки активационной ссылки
     * на электронную почту пользователя.
     *
     * @return    Ответ, указывающий на успешную отправку активационной ссылки.
     */
    @GetMapping("/send-activation-link")
    @ResponseStatus(HttpStatus.OK)
    public void sendActivationLink(@RequestParam String email) {
        accountActivationService.sendActivationLink(email);
    }
}
