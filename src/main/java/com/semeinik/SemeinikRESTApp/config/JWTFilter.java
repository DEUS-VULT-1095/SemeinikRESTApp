package com.semeinik.SemeinikRESTApp.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.semeinik.SemeinikRESTApp.security.JWTUtil;
import com.semeinik.SemeinikRESTApp.services.PersonDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * Класс JWT фильтра, который выполняет проверку и аутентификацию JWT токенов при каждом HTTP-запросе.
 * Если в заголовке запроса присутствует действительный JWT токен, данный фильтр извлекает информацию
 * о пользователе из токена и добавляет ее в контекст безопасности.
 * @see OncePerRequestFilter
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil; // Утилита для генерации и верификации JWT токенов
    private final PersonDetailsService personDetailsService; // Сервис для предоставления информации о пользователях

    /**
     * Конструктор класса JWTFilter.
     *
     * @param jwtUtil              Утилита для работы с JWT токенами ({@link JWTUtil}).
     * @param personDetailsService Сервис для работы с данными пользователей ({@link PersonDetailsService}).
     */
    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

    /**
     * Метод фильтра, который обрабатывает каждый HTTP-запрос.
     * Если в запросе присутствует действительный JWT токен, фильтр извлекает информацию о пользователе из него
     * и добавляет аутентифицированного пользователя в контекст безопасности.
     *
     * @param request     HTTP-запрос.
     * @param response    HTTP-ответ.
     * @param filterChain Цепочка фильтров.
     * @throws ServletException      Если возникает ошибка связанная с сервлетом.
     * @throws IOException           Если возникает ошибка ввода/вывода.
     * @throws JWTVerificationException Если JWT токен не прошел верификацию.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            try {
                // Верификация JWT токена и извлечение его данных
                Map<String, Claim> claims = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);

                // Загрузка информации о пользователе на основе email из токена
                UserDetails userDetails = personDetailsService.loadUserByUsername(claims.get("email").asString());

                // Создание аутентификационного токена и добавление его в контекст безопасности
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                                userDetails.getAuthorities());

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (JWTVerificationException ex) {
                // В случае недействительного JWT токена отправляем статус UNAUTHORIZED
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid jwt token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
