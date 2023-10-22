package com.semeinik.SemeinikRESTApp.services;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.semeinik.SemeinikRESTApp.dto.AuthDTO;
import com.semeinik.SemeinikRESTApp.exceptions.ActivationAccountException;
import com.semeinik.SemeinikRESTApp.exceptions.RequestNotContainCookiesException;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.models.Session;
import com.semeinik.SemeinikRESTApp.security.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

/**
 * Класс {@code AuthenticationLogoutService} представляет сервис, отвечающий за аутентификацию,
 * а также создание и управление токенами для входа и выхода из системы.
 *
 * Этот сервис реализует следующие функции:
 * - Аутентификация пользователя на основе предоставленных учетных данных (email и пароль).
 * - Создание и валидация JSON Web Token (JWT) для аутентифицированных пользователей.
 * - Создание и управление сеансами пользователей, включая генерацию и сохранение куки-токенов.
 * - Операции выхода из системы и обновления токенов.
 *
 * Этот класс использует аннотацию {@code @Service}, чтобы быть компонентом Spring.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Service
public class AuthenticationLogoutService {
    private final PeopleService peopleService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final SessionsService sessionsService;

    /**
     * Конструктор класса {@code AuthenticationLogoutService} с инъекцией зависимостей.
     *
     * @param peopleService           Сервис для работы с пользователями ({@link PeopleService}).
     * @param authenticationManager   Менеджер аутентификации для проверки учетных данных ({@link AuthenticationManager}).
     * @param jwtUtil                 Утилита для создания и валидации JWT-токенов ({@link JWTUtil}).
     * @param sessionsService         Сервис для управления сеансами пользователей ({@link SessionsService}).
     */
    @Autowired
    public AuthenticationLogoutService(PeopleService peopleService, AuthenticationManager authenticationManager, JWTUtil jwtUtil, SessionsService sessionsService) {
        this.peopleService = peopleService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.sessionsService = sessionsService;
    }

    /**
     * Метод выполняет аутентификацию пользователя на основе предоставленных учетных данных.
     * В случае успешной аутентификации, создается JWT-токен и сессия пользователя сохраняется в базе данных.
     *
     * @param authDTO   Объект {@link AuthDTO}, содержащий учетные данные пользователя (email и пароль).
     * @param response  Ответ HTTP, используется для добавления куки-токена в ответ.
     * @return          Сгенерированный JWT-токен.
     * @throws BadCredentialsException       В случае неверных учетных данных.
     * @throws ActivationAccountException    Если аккаунт пользователя не активирован.
     */
    @Transactional
    public String performLogin(AuthDTO authDTO, HttpServletResponse response) {
        Person person = peopleService.findByEmail(authDTO.getEmail()).orElseThrow(() ->
                new BadCredentialsException("Invalid email"));

        String salt = person.getSalt();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword() + salt);

        try {
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!person.isActivated()) {
            throw new ActivationAccountException("Login is not available without email confirmation");
        }

        Cookie cookie = sessionsService.generateCookieAndSaveSession(person);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return jwtUtil.generateToken(person.getId(), person.getEmail(), person.getRole(), person.getFamily());
    }

    /**
     * Метод выполняет выход пользователя из системы путем удаления сессии из БД
     * и установки срока действия куки-токена в ноль.
     *
     * @param request   Запрос HTTP, используется для получения куки-токена.
     * @param response  Ответ HTTP, используется для обновления куки-токена.
     * @throws InvalidCookieException Если предоставлен не валидный куки.
     */
    @Transactional
    public void performLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("refreshToken")).findFirst()
                .orElseThrow(() -> new InvalidCookieException("Cookie not contain key \"refreshToken\""));

        UUID refreshToken = UUID.fromString(cookie.getValue());

        sessionsService.deleteByRefreshToken(refreshToken);

        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    /**
     * Метод выполняет обновление JWT-токена, если они истек и обновление куки-токена.
     *
     * @param request   Запрос HTTP, используется для получения JWT и куки-токена.
     * @param response  Ответ HTTP, используется для обновления куки-токена.
     * @return          Новый JWT-токен, если обновление прошло успешно.
     * @throws InvalidCookieException   В случае неверного или отсутствующего куки-токена.
     * @throws TokenExpiredException    Если токен истек и не может быть обновлен.
     */
    public String performRefreshTokens(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("refreshToken")).findFirst()
                .orElseThrow(() -> new InvalidCookieException("Cookie not contain key \"refreshToken\""));

        Session session = sessionsService.findByRefreshToken(UUID.fromString(cookie.getValue())).
                orElseThrow(() -> new InvalidCookieException("Invalid cookie. Cookie not found in Data base."));

        sessionsService.delete(session);

        ZonedDateTime expiresIn = session.getExpiresIn().minusMinutes(1);

        if (expiresIn.isBefore(ZonedDateTime.now())) {
            throw new TokenExpiredException("Refresh Token expired", expiresIn.toInstant());
        }

        Person person = session.getPerson();
        Cookie newCookie = sessionsService.generateCookieAndSaveSession(person);
        newCookie.setHttpOnly(true);
        response.addCookie(newCookie);

        return jwtUtil.generateToken(person.getId(), person.getEmail(), person.getRole(), person.getFamily());
    }
}
