package com.semeinik.SemeinikRESTApp.security;

import com.semeinik.SemeinikRESTApp.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * Реализация интерфейса {@link UserDetails} для класса {@link Person}.
 *
 * Этот класс предоставляет информацию о пользователе ({@link Person}) в форме, пригодной для использования
 * в механизме аутентификации Spring Security. Он реализует интерфейс {@link UserDetails} и предоставляет
 * информацию о роли человека, его пароле и статусе учетной записи.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
public class PersonDetails implements UserDetails {
    private final Person person;

    /**
     * Конструктор класса, который принимает объект {@link Person}.
     *
     * @param person Пользователь ({@link Person}), для которого создается объект PersonDetails.
     */
    public PersonDetails(Person person) {
        this.person = person;
    }

    /**
     * Возвращает коллекцию ролей пользователя ({@link Person}).
     *
     * @return Коллекция ролей пользователя в форме объектов {@link GrantedAuthority}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    /**
     * Возвращает пароль пользователя ({@link Person}).
     *
     * @return Пароль пользователя в захешированном или не захешированном виде.
     */
    @Override
    public String getPassword() {
        return person.getPassword();
    }

    /**
     * Возвращает email пользователя ({@link Person}), используемый как имя пользователя ({@link Person}).
     *
     * @return Email пользователя ({@link Person}).
     */
    @Override
    public String getUsername() {
        return person.getEmail();
    }

    /**
     * Проверяет, истёк ли срок действия учётной записи.
     *
     * @return Всегда возвращает true, так как срок действия учетной записи не ограничен.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, заблокирована ли учетная запись пользователя ({@link Person}).
     *
     * @return Всегда возвращает true, так как учетная запись не заблокирована.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, истек ли срок действия учетных данных пользователя ({@link Person}) (например, пароля).
     *
     * @return Всегда возвращает true, так как срок действия учетных данных не ограничен.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, включена ли учетная запись пользователя ({@link Person}).
     *
     * @return Всегда возвращает true, так как учетная запись всегда включена.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Person getPerson() {
        return person;
    }
}
