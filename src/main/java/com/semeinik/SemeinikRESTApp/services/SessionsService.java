package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.models.Session;
import com.semeinik.SemeinikRESTApp.repositories.SessionsRepository;
import com.semeinik.SemeinikRESTApp.utils.CookieGenerator;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для управления сессиями ({@link Session}).
 * Этот сервис предоставляет методы для сохранения, поиска и удаления сессий.
 * Он также выполняет очистку старых сессий для каждого пользователя, если
 * количество сессий достигает предела (5 сессий).
 *
 * Внимание: Этот класс является сервисом, помеченным аннотацией {@code @Service},
 * и имеет правила транзакций с аннотацией {@code @Transactional}.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class SessionsService {
    private final SessionsRepository sessionsRepository;

    /**
     * Конструктор класса. Инъектирует {@link SessionsRepository} в качестве зависимости.
     *
     * @param sessionsRepository Репозиторий для доступа к данным о сессиях ({@link Session}).
     */
    @Autowired
    public SessionsService(SessionsRepository sessionsRepository) {
        this.sessionsRepository = sessionsRepository;
    }

    /**
     * Сохраняет сессию.
     * Если количество сессий для пользователя достигает предела (5 сессий),
     * удаляет старые сессии перед сохранением новой.
     *
     */
    @Transactional()
    public Cookie generateCookieAndSaveSession(Person person) {
        Cookie cookie;

        do {
            cookie = CookieGenerator.generateCookie();
        } while (sessionsRepository.findByRefreshToken(UUID.fromString(cookie.getValue())).isPresent());

        Session session = new Session(UUID.fromString(cookie.getValue()), ZonedDateTime.now().plusSeconds(cookie.getMaxAge()),
                ZonedDateTime.now(), person);

        List<Session> sessions = sessionsRepository.findAllByPersonId(session.getPerson().getId());

        if (sessions.size() >= 5) {
            sessionsRepository.deleteAllByPersonId(session.getPerson().getId());
        }

        sessionsRepository.save(session);

        return cookie;
    }

    /**
     * Ищет сессию по токену обновления (refresh token).
     *
     * @param refreshToken Токен обновления для поиска сессии.
     * @return Сессия, если найдена, в противном случае пустое значение {@code Optional}.
     */
    public Optional<Session> findByRefreshToken(UUID refreshToken) {
        return sessionsRepository.findByRefreshToken(refreshToken);
    }

    /**
     * Удаляет сессию по токену обновления (refresh token).
     *
     * @param refreshToken Токен обновления для удаления сессии.
     */
    @Transactional
    public void deleteByRefreshToken(UUID refreshToken) {
        sessionsRepository.deleteByRefreshToken(refreshToken);
    }

    /**
     * Удаляет сессию.
     *
     * @param session Сессия для удаления ({@link Session}).
     */
    @Transactional
    public void delete(Session session) {
        sessionsRepository.delete(session);
    }
}
