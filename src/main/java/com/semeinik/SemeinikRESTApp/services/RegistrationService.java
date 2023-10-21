package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.mappers.FamilyMapper;
import com.semeinik.SemeinikRESTApp.mappers.PersonMapper;
import com.semeinik.SemeinikRESTApp.models.ActivationToken;
import com.semeinik.SemeinikRESTApp.models.Family;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.utils.ActivationTokenGenerator;
import com.semeinik.SemeinikRESTApp.utils.SaltGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.semeinik.SemeinikRESTApp.dto.PersonDTO;
import com.semeinik.SemeinikRESTApp.dto.FamilyDTO;

import java.util.UUID;

/**
 * Класс {@code RegistrationService} предоставляет сервисные методы для регистрации пользователей
 * и создания семей в системе. Этот класс обеспечивает взаимодействие между сущностями {@code Person}
 * и {@code Family}, выполняя необходимые операции создания и сохранения в базе данных.
 *
 * Этот класс использует аннотацию {@code @Service}, чтобы быть компонентом Spring.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Service
public class RegistrationService {
    private final PeopleService peopleService;
    private final EmailService emailService;
    private final ActivationTokensService activationTokensService;

    /**
     * Конструктор класса {@code RegistrationService} с инъекцией зависимостей.
     *
     * @param peopleService            Сервис для работы с пользователями ({@link PeopleService}).
     * @param emailService             Сервис для отправки электронных писем ({@link EmailService}).
     * @param activationTokensService  Сервис для работы с активационными токенами ({@link ActivationTokensService}).
     */
    @Autowired
    public RegistrationService( PeopleService peopleService, EmailService emailService,
                                ActivationTokensService activationTokensService) {
        this.peopleService = peopleService;
        this.emailService = emailService;
        this.activationTokensService = activationTokensService;
    }

    /**
     * Выполняет регистрацию пользователя. Включает в себя назначение соли, хэширование пароля, назначение роли юзера,
     * генерацию, сохранение и отправку активационного токена на почту.
     *
     * @param personDTO {@link PersonDTO} получаемый от клиента.
     */
    @Transactional()
    public void performRegistrationPerson(PersonDTO personDTO) {
        Person person = peopleService.savePerson(personDTO);
        ActivationToken activationToken = activationTokensService.generateActivationTokenAndSave(person);
        person.setActivationToken(activationToken);
        emailService.sendActivationEmail(person.getEmail(), person.getActivationToken().getToken().toString());
    }
}
