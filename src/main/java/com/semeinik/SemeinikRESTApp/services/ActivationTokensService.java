package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.models.ActivationToken;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.repositories.ActivationTokensRepository;
import com.semeinik.SemeinikRESTApp.utils.ActivationTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс сервиса для работы с активационными токенами ({@link ActivationToken}).
 *
 * Этот класс предоставляет методы для создания, поиска и удаления активационных токенов.
 * @Transactional(readOnly = true) Эта аннотация означает, что все методы этого класса будут выполняться внутри транзакций
 * только для чтения. Если метод выполняет не только чтение, нужно пометить его аннотацией {@code @Transactional}.
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ActivationTokensService {
    private final ActivationTokensRepository activationTokensRepository;

    /**
     * Конструктор класса, принимающий репозиторий активационных токенов ({@link ActivationTokensRepository}) в качестве зависимости.
     *
     * @param activationTokensRepository Репозиторий активационных токенов ({@link ActivationTokensRepository}).
     */
    @Autowired
    public ActivationTokensService(ActivationTokensRepository activationTokensRepository) {
        this.activationTokensRepository = activationTokensRepository;
    }

    /**
     * Поиск активационного токена ({@link ActivationToken}) по его значению (UUID).
     *
     * @param token Значение активационного токена ({@link ActivationToken}) (UUID).
     * @return Опциональный объект, содержащий активационный токен ({@link ActivationToken}), если он найден, или пустой,
     * если токен не существует.
     */
    public Optional<ActivationToken> findByToken(UUID token) {
        return activationTokensRepository.findByToken(token);
    }

    /**
     * Генерация и сохранение активационного токена.
     *
     * @param activationToken Токен для активации аккаунта {@link ActivationToken}.
     */
    @Transactional
    public void saveToken(ActivationToken activationToken) {
        activationTokensRepository.save(activationToken);
    }

    @Transactional
    public ActivationToken generateActivationTokenAndSave(Person person) {
        UUID token;

        do {
            token = ActivationTokenGenerator.generateActivationToken();
        } while (findByToken(token).isPresent());

        int daysOfValidity = 1;
        ActivationToken activationToken = new ActivationToken(
                token,
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(daysOfValidity),
                person);
        saveToken(activationToken);

        return activationToken;
    }

    /**
     * Удаление активационного токена ({@link ActivationToken}).
     *
     * @param activationToken Активационный токен ({@link ActivationToken}), который нужно удалить.
     */
    @Transactional
    public void delete(ActivationToken activationToken) {
        activationTokensRepository.delete(activationToken);
    }
}
