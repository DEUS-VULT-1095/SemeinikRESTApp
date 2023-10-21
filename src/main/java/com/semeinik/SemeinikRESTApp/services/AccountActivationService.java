package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.exceptions.ActivationAccountException;
import com.semeinik.SemeinikRESTApp.exceptions.ActivationTokenNotFoundException;
import com.semeinik.SemeinikRESTApp.exceptions.InvalidActivationTokenException;
import com.semeinik.SemeinikRESTApp.exceptions.PersonNotFoundException;
import com.semeinik.SemeinikRESTApp.models.ActivationToken;
import com.semeinik.SemeinikRESTApp.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Класс {@code AccountActivationService} представляет собой сервис, отвечающий за активацию учетных записей
 * пользователей. Этот сервис обеспечивает активацию учетных записей с помощью активационных токенов и
 * отправку активационных писем для завершения процедуры регистрации.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Service
public class AccountActivationService {
    private final ActivationTokensService activationTokensService;
    private final PeopleService peopleService;
    private final EmailService emailService;

    /**
     * Конструктор сервиса для работы с активацией аккаунта.
     *
     * @param activationTokensService  Сервис для работы с активационными токенами {@link ActivationToken} ({@link ActivationTokensService}).
     * @param peopleService            Сервис для работы с пользователями {@link Person} ({@link PeopleService}).
     * @param emailService             Сервия для отправки писем на email ({@link EmailService}).
     */
    @Autowired
    public AccountActivationService(ActivationTokensService activationTokensService, PeopleService peopleService,
                                    EmailService emailService) {
        this.activationTokensService = activationTokensService;
        this.peopleService = peopleService;
        this.emailService = emailService;
    }

    /**
     * Метод {@code activateAccount} активирует учетную запись пользователя на основе активационного токена.
     *
     * @param activationToken Строка, представляющая активационный токен пользователя.
     * @throws ActivationTokenNotFoundException Если активационный токен не найден в базе данных.
     * @throws InvalidActivationTokenException  Если активационный токен устарел и срок его действия истек.
     * @throws IllegalArgumentException         Если переданная строка не соответствует формату UUID.
     */
    @Transactional
    public void activateAccount(String activationToken) {
        try {
            ActivationToken token = activationTokensService.findByToken(UUID.fromString(activationToken))
                    .orElseThrow(() -> new ActivationTokenNotFoundException("Activation token not found"));

            if (token.getExpiresAt().isBefore(ZonedDateTime.now())) {
                throw new InvalidActivationTokenException("Activation token expired");
            }

            Person person = token.getPerson();
            person.setActivated(true);
            activationTokensService.delete(token); // удаляем использованный токен активации из БД
            person.setActivationToken(null); // следуем хорошей практике и обнуляем токен и на стороне Person тоже

        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Wrong UUID format");
        }
    }

    /**
     * Метод {@code sendActivationLink} отправляет активационное письмо на email пользователя для завершения
     * процедуры регистрации.
     *
     * @param email Почта, на которую отправляется ссылка с активационным токеном.
     * @throws PersonNotFoundException    Если учетная запись пользователя не найдена.
     * @throws ActivationAccountException Если учетная запись уже активирована.
     */
    @Transactional
    public void sendActivationLink(String email) {
        Person person = peopleService.findByEmail(email)
                .orElseThrow(() -> new PersonNotFoundException("Person with email \"" + email + "\" not found"));

        if (person.isActivated()) {
            throw new ActivationAccountException("This account email \"" + person.getEmail() + "\" confirmed");
        }

        if (person.getActivationToken() != null) {
            activationTokensService.delete(person.getActivationToken());
        }

        ActivationToken activationToken = activationTokensService.generateActivationTokenAndSave(person);
        person.setActivationToken(activationToken);
        emailService.sendActivationEmail(person.getEmail(), activationToken.getToken().toString());
    }
}
