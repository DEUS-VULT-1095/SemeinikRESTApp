package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.dto.PersonDTO;
import com.semeinik.SemeinikRESTApp.mappers.PersonMapper;
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
 * Этот класс предоставляет методы для управления данными пользователей, такими как сохранение, поиск, редактирование,
 * удаление и т.п.
 *
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
    private final PersonMapper personMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор класса, принимающий {@link PeopleRepository}.
     *
     * @param peopleRepository Репозиторий для работы с данными пользователей({@link PeopleRepository}).
     * @param personMapper     Маппер для преобразования данных {@link PersonDTO} в сущность {@link Person} и наоборот.
     * @param passwordEncoder  Инструмент для кодирования паролей ({@link PasswordEncoder}).
     */
    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PersonMapper personMapper, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.personMapper = personMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Сохранение пользователя в БД, предварительно назначая поля.
     *
     * @param personDTO {@link PersonDTO} Объект передачи данных пользователя {@link Person}
     */
    @Transactional
    public Person savePerson(PersonDTO personDTO) {
        Person person = personMapper.convertToPerson(personDTO);
        String salt;

        do {
            salt = SaltGenerator.generateSalt();
        } while (findBySalt(salt).isPresent());

        person.setSalt(salt);
        person.setPassword(passwordEncoder.encode(person.getPassword() + salt));
        person.setRole("ROLE_USER");
        peopleRepository.save(person);

        return person;
    }

    /**
     * Поиск пользователя по соли.
     * @param salt Соль.
     * @return Данные пользователя, соответствующие указанной соли, если он существет.
     */
    public Optional<Person> findBySalt(String salt) {
        return peopleRepository.findBySalt(salt);
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
