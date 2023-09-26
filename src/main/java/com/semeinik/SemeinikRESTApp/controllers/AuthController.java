package com.semeinik.SemeinikRESTApp.controllers;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.semeinik.SemeinikRESTApp.dto.*;
import com.semeinik.SemeinikRESTApp.exceptions.ActivationAccountException;
import com.semeinik.SemeinikRESTApp.exceptions.InvalidEmailException;
import com.semeinik.SemeinikRESTApp.exceptions.RequestNotContainCookiesException;
import com.semeinik.SemeinikRESTApp.models.Family;
import com.semeinik.SemeinikRESTApp.models.Person;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.semeinik.SemeinikRESTApp.utils.ErrorMsgCreator.createErrorMsg;

/**
 * Контроллер для аутентификации, регистрации и управления сессиями пользователей.
 * Этот контроллер предоставляет различные эндпоинты для регистрации пользователей, входа в систему, а также управления сессиями и токенами.
 * @RestController указывает, что класс представляет собой REST-контроллер.
 * @RequestMapping задает базовый путь для всех запросов, обрабатываемых контроллером.
 * @author Denis Kolesnikov
 * @version 1.0
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
     * @param personDTOValidator    Валидатор DTO польщователя ({@link PersonDTOValidator}).
     * @param peopleService         Сервис для работы с данными пользователей ({@link PeopleService}).
     * @param registrationService   Сервис для работы с регистрацией пользователей ({@link RegistrationService}).
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
     * Метод выполняет регистрацию пользователя и создание семьи на основе предоставленных данных.
     *
     * @param registrationRequestWithCreateFamily Запрос на регистрацию, содержащий данные пользователя и семьи ({@link RegistrationRequestWithCreateFamily}).
     * @param bindingResult                       Результат валидации запроса.
     * @return ResponseEntity с ответом на запрос, включая идентификатор семьи и статус HttpStatus.CREATED, если регистрация успешна.
     * @throws PersonNotAddedException если возникли ошибки при добавлении пользователя.
     * @see RegistrationRequestWithCreateFamily
     * @see PersonDTOValidator
     * @see Person
     * @see Family
     */
    @PostMapping("/register-person-and-create-family")
    public ResponseEntity<RegistrationResponseWithCreateFamily> performRegistrationAndCreateFamily(@RequestBody @Valid RegistrationRequestWithCreateFamily registrationRequestWithCreateFamily,
                                                                                                   BindingResult bindingResult) {

        personDTOValidator.validate(registrationRequestWithCreateFamily.getPersonDTO(), bindingResult);

        if (bindingResult.hasErrors()) {
            throw new PersonNotAddedException(createErrorMsg(bindingResult));
        }

        RegistrationResponseWithCreateFamily response = registrationService.performRegistrationAndCreateFamily(registrationRequestWithCreateFamily);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод выполняет регистрацию пользователя и присоединение к уже существующей семье на основе предоставленных данных.
     *
     * @param registrationRequestJoinFamily Запрос на регистрацию и присоединение к семье, содержащий данные
     *                                      пользователя и идентификатор семьи ({@link RegistrationRequestJoinFamily}).
     * @param bindingResult                 Результат валидации запроса.
     * @return ResponseEntity со статусом HttpStatus.CREATED, если регистрация успешна.
     * @throws PersonNotAddedException если возникли ошибки при добавлении пользователя.
     * @see RegistrationRequestJoinFamily
     * @see PersonDTOValidator
     * @see Person
     * @see Family
     */
    @PostMapping("/register-person-and-join-the-family")
    public ResponseEntity<Object> performRegistrationAndJoinTheFamily(@RequestBody @Valid RegistrationRequestJoinFamily registrationRequestJoinFamily,
                                                                      BindingResult bindingResult) {

        personDTOValidator.validate(registrationRequestJoinFamily.getPersonDTO(), bindingResult);

        if (bindingResult.hasErrors()) {
            throw new PersonNotAddedException(createErrorMsg(bindingResult));
        }

        registrationService.performRegistrationAndJoinTheFamily(registrationRequestJoinFamily);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Метод выполняет запрос на аутентификацию пользователя. После успешной аутентификации возвращает JWT в теле ответа.
     *
     * @param authDTO   Объект, содержащий учётные данные пользователя: email и пароль.
     * @param response  Ответ, в который будет добавлен JWT-токен и в который будет добавлен куки "refreshToken".
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
     * @param emailDTO      DTO (Data Transfer Object) с адресом электронной почты для проверки ({@link EmailDTO}).
     * @param bindingResult Результаты проверки входных данных.
     * @return ResponseEntity с булевым значением, указывающим наличие пользователя с заданным адресом электронной почты,
     * и статусом HttpStatus.OK, если проверка выполнена успешно.
     * @throws InvalidEmailException если входные данные не содержат email-формат.
     */
    @PostMapping("/exist-email")
    public ResponseEntity<Boolean> existEmail(@RequestBody @Valid EmailDTO emailDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidEmailException(createErrorMsg(bindingResult));
        }

        return ResponseEntity.ok(peopleService.findByEmail(emailDTO.getEmail()).isPresent());
    }

    // тестовый метод
    @GetMapping("/info")
    public ResponseEntity<HttpStatus> getInfo() {
        System.out.println("info");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
