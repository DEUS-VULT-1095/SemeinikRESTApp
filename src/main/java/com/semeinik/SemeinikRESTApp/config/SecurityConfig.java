package com.semeinik.SemeinikRESTApp.config;

import com.semeinik.SemeinikRESTApp.controllers.GlobalExceptionHandler;
import com.semeinik.SemeinikRESTApp.services.PersonDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Класс конфигурации безопасности Spring Security. Он определяет настройки безопасности
 * для вашего приложения, включая правила аутентификации и авторизации, использование JWT токенов
 * для аутентификации и другие аспекты безопасности.
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final PersonDetailsService personDetailsService; // Сервис для работы с данными пользователей
    private final JWTFilter jwtFilter; // Фильтр для обработки JWT токенов

    /**
     * Конструктор класса SecurityConfig.
     *
     * @param personDetailsService Сервис для работы с данными пользователей ({@link PersonDetailsService}).
     * @param jwtFilter           Фильтр для обработки JWT токенов ({@link JWTFilter}).
     */
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
    }

    /**
     * Конфигурация цепочки фильтров безопасности.
     * Здесь определяются правила аутентификации и авторизации, а также применяется фильтр JWT для проверки токенов.
     *
     * @param httpSecurity Объект для настройки безопасности HTTP-запросов.
     * @return Объект цепочки фильтров безопасности.
     * @throws Exception Если возникают ошибки при настройке безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/register-person-and-create-family", "/auth/register-person-and-join-the-family",
                        "/auth/login", "/auth/refresh-tokens", "/activate/**", "/auth/email-exist").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                //.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) //вот здесь я реализовал выброс 401 статуса (в данный момент реализовано в самом фильтре)
                //.and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Указываем что у нашего приложения не будет состояния(нет сессий)

        return httpSecurity.build();
    }

    /**
     * Конфигурация менеджера аутентификации.
     * Здесь определяется, каким образом будут аутентифицироваться пользователи, используя сервис {@link PersonDetailsService}.
     *
     * @param httpSecurity Объект для настройки безопасности HTTP-запросов.
     * @return Менеджер аутентификации.
     * @throws Exception Если возникают ошибки при настройке менеджера аутентификации.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(personDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * Конфигурация кодировщика паролей.
     * Здесь определяется, как будут кодироваться пароли пользователей, используя BCryptPasswordEncoder.
     *
     * @return Кодировщик паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
