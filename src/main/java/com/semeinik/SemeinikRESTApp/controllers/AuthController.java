package com.semeinik.SemeinikRESTApp.controllers;

import com.semeinik.SemeinikRESTApp.dto.*;
import com.semeinik.SemeinikRESTApp.exceptions.InvalidEmailException;
import com.semeinik.SemeinikRESTApp.security.JWTUtil;
import com.semeinik.SemeinikRESTApp.services.*;
import com.semeinik.SemeinikRESTApp.utils.PersonDTOValidator;
import com.semeinik.SemeinikRESTApp.exceptions.PersonNotAddedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.semeinik.SemeinikRESTApp.utils.ErrorMsgCreator.createErrorMsg;

/**
 * Контроллер для аутентификации, регистрации и управления сессиями пользователей.
 * Этот контроллер предоставляет различные эндпоинты для регистрации пользователей, входа в систему, а также управления сессиями и токенами.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 * @RestController указывает, что класс представляет собой REST-контроллер.
 * @RequestMapping задает базовый путь для всех запросов, обрабатываемых контроллером.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonDTOValidator personDTOValidator;
    private final PeopleService peopleService;
    private final RegistrationService registrationService;
    private final AuthenticationLogoutService authenticationLogoutService;


    /**
     * Конструктор класса AuthController.
     *
     * @param personDTOValidator          Валидатор DTO польщователя ({@link PersonDTOValidator}).
     * @param peopleService               Сервис для работы с данными пользователей ({@link PeopleService}).
     * @param registrationService         Сервис для работы с регистрацией пользователей ({@link RegistrationService}).
     * @param authenticationLogoutService Сервис для работы с аутентификацией и логаутом ({@link AuthenticationLogoutService}).
     */
    @Autowired
    public AuthController(PersonDTOValidator personDTOValidator, PeopleService peopleService,
                          RegistrationService registrationService, AuthenticationLogoutService authenticationLogoutService) {
        this.personDTOValidator = personDTOValidator;
        this.peopleService = peopleService;
        this.registrationService = registrationService;
        this.authenticationLogoutService = authenticationLogoutService;
    }

    /**
     * Выполняет регистрацию пользователя.
     *
     * @param personDTO     {@link PersonDTO} Объект, содержащий данные пользователя для регистрации.
     * @param bindingResult Результаты проверки входных данных.
     */
    @PostMapping("/register-person")
    @ResponseStatus(HttpStatus.CREATED)
    public void performRegistrationPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {

        personDTOValidator.validate(personDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new PersonNotAddedException(createErrorMsg(bindingResult));
        }

        registrationService.performRegistrationPerson(personDTO);
    }

    /**
     * Метод выполняет запрос на аутентификацию пользователя. После успешной аутентификации возвращает JWT в теле ответа.
     *
     * @param authDTO  Объект, содержащий учётные данные пользователя: email и пароль.
     * @param response Ответ, в который будет добавлен JWT-токен и в который будет добавлен куки "refreshToken".
     * @return ResponseEntity Ответ на запрос, включая JWT-токен, и статус HttpStatus.OK, если вход успешен.
     * @see AuthDTO
     * @see JWTUtil
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody AuthDTO authDTO, HttpServletResponse response) {
        String jwtToken = authenticationLogoutService.performLogin(authDTO, response);

        return ResponseEntity.ok(Map.of("jwtToken", jwtToken));
    }

    /**
     * Метод обрабатывает запрос на выход пользователя из учётной записи.
     *
     * @param request  Запрос, в котором ожидается куки-токен "refreshToken".
     * @param response Ответ, в который будет добавлен обновленный куки-токен "refreshToken" с максимальным сроком жизни 0,
     *                 чтобы удалить его с клиента.
     * @return ResponseEntity со статусом HttpStatus.OK, если выход пользователя успешно выполнен.
     */
    @PostMapping("/logout")
    public ResponseEntity<Object> performLogout(HttpServletRequest request, HttpServletResponse response) {
        authenticationLogoutService.performLogout(request, response);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Метод обновляет JWT-токен и куки-токен пользователя, используя куки-токен "refreshToken".
     *
     * @param request  Запрос, в котором ожидается куки-токен "refreshToken".
     * @param response Ответ, в который будет добавлен новый куки-токен и обновленный JWT-токен.
     * @return ResponseEntity с данными обновленного JWT-токена и статусом HttpStatus.OK, если обновление успешно выполнено.
     */
    @PostMapping("/refresh-tokens")
    public ResponseEntity<?> performRefreshTokens(HttpServletRequest request, HttpServletResponse response) {
        String jwtToken = authenticationLogoutService.performRefreshTokens(request, response);

        return ResponseEntity.ok(Map.of("jwtToken", jwtToken));
    }

    /**
     * Проверяет наличие пользователя с заданным адресом электронной почты в БД.
     *
     * @param email Адрес электронной почты для проверки, есть ли он уже в БД.
     * @return ResponseEntity с Мапой с ключом строки и булевым значением, указывающим наличие пользователя с заданным
     * адресом электронной почты и статусом HttpStatus.OK, если проверка выполнена успешно.
     */
    @GetMapping("/exist-email")
    public ResponseEntity<Map<String, Boolean>> existEmail(@RequestParam String email) {

        return ResponseEntity.ok(Map.of("isExist", peopleService.findByEmail(email).isPresent()));
    }

    // тестовый метод
    @GetMapping("/info")
    public ResponseEntity<HttpStatus> getInfo() {
        System.out.println("info");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
