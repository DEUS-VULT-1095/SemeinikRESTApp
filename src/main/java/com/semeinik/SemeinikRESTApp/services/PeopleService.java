package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.models.ActivationToken;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.repositories.PeopleRepository;
import com.semeinik.SemeinikRESTApp.utils.SaltGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервисный класс для работы с данными пользователей ({@link Person}).
 *
 * Этот класс предоставляет методы для управления данными пользователей, такими как сохранение новых данных о пользователе,
 * поиск пользователя по адресу электронной почты и генерацию и отправку электронных писем для активации учетных записей.
 * @Transactional(readOnly = true) Эта аннотация означает, что все методы этого класса будут выполняться внутри транзакций
 * только для чтения. Если метод выполняет не только чтение, нужно пометить его аннотацией {@code @Transactional}.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivationTokensService activationTokensService;

    /**
     * Конструктор класса, принимающий {@link PeopleRepository}, {@link PasswordEncoder}, {@link EmailService} и
     * {@link ActivationTokensService} в качестве зависимостей.
     * @param peopleRepository       Репозиторий для работы с данными пользователей({@link PeopleRepository}).
     * @param passwordEncoder        Инструмент для кодирования паролей ({@link PasswordEncoder}).
     * @param activationTokensService Сервис для работы с активационными токенами ({@link ActivationTokensService}).
     */
    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, ActivationTokensService activationTokensService) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.activationTokensService = activationTokensService;
    }

    /**
     * Сохранение новых данных пользователя, включая кодирование пароля, генерацию и сохранение активационного токена
     * ({@link ActivationToken}).
     *
     * @param person Данные пользователя ({@link Person}) для сохранения.
     */
    @Transactional
    public void createPersonWithActivationToken(Person person) {
        String salt;

        do {
            salt = SaltGenerator.generateSalt();
        } while (peopleRepository.findBySalt(salt).isPresent());

        person.setSalt(salt);

        person.setPassword(passwordEncoder.encode(person.getPassword() + salt));

        person.setRole("ROLE_USER");

        ActivationToken activationToken = activationTokensService.generateActivationTokenAndSave(person);

        person.setActivationToken(activationToken);

        peopleRepository.save(person);
    }

    /**
     * Поиск пользователя по адресу электронной почты.
     *
     * @param email Адрес электронной почты для поиска.
     * @return Данные пользователя, соответствующие указанному адресу электронной почты, если он существет.
     */
    public Optional<Person> findByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }
}
