package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.repositories.PeopleRepository;
import com.semeinik.SemeinikRESTApp.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Класс сервиса для аутентификации пользователей ({@link Person}) по их адресу электронной почты.
 *
 * Этот класс реализует интерфейс {@link UserDetailsService} Spring Security и предоставляет метод для загрузки
 * информации о пользователе по его адресу электронной почты.
 * @Transactional(readOnly = true) Эта аннотация означает, что все методы этого класса будут выполняться внутри транзакций
 * только для чтения. Если метод выполняет не только чтение, нужно пометить его аннотацией {@code @Transactional}.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    /**
     * Конструктор класса, принимающий {@link PeopleRepository} в качестве зависимости.
     *
     * @param peopleRepository Репозиторий для работы с данными пользователей ({@link PeopleRepository}).
     */
    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    /**
     * Загрузка информации о пользователе по его адресу электронной почты.
     *
     * @param email Адрес электронной почты пользователя ({@link Person}) для поиска.
     * @return Данные о пользователе ({@link Person}), если он найден, в противном случае выбрасывается исключение UsernameNotFoundException.
     * @throws UsernameNotFoundException Если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Person> person = peopleRepository.findByEmail(email);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Email not found");
        }

        return new PersonDetails(person.get());
    }
}
