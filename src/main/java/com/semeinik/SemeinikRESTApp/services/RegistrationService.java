package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.dto.RegistrationRequestJoinFamily;
import com.semeinik.SemeinikRESTApp.dto.RegistrationRequestWithCreateFamily;
import com.semeinik.SemeinikRESTApp.dto.RegistrationResponseWithCreateFamily;
import com.semeinik.SemeinikRESTApp.exceptions.FamilyNotFoundException;
import com.semeinik.SemeinikRESTApp.mappers.FamilyMapper;
import com.semeinik.SemeinikRESTApp.mappers.PersonMapper;
import com.semeinik.SemeinikRESTApp.models.Family;
import com.semeinik.SemeinikRESTApp.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.semeinik.SemeinikRESTApp.dto.PersonDTO;
import com.semeinik.SemeinikRESTApp.dto.FamilyDTO;

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
    private final PersonMapper personMapper;
    private final FamilyMapper familyMapper;
    private final PeopleService peopleService;
    private final FamilyService familyService;
    private final EmailService emailService;

    /**
     * Конструктор класса {@code RegistrationService} с инъекцией зависимостей.
     *
     * @param personMapper     Маппер для преобразования данных {@link PersonDTO} в сущность {@link  Person}.
     * @param familyMapper     Маппер для преобразования данных {@link FamilyDTO} в сущность {@link Family}.
     * @param peopleService    Сервис для работы с пользователями ({@link PeopleService}).
     * @param familyService    Сервис для работы с семьями ({@link FamilyService}).
     * @param emailService     Сервис для отправки электронных писем ({@link EmailService}).
     */
    @Autowired
    public RegistrationService(PersonMapper personMapper, FamilyMapper familyMapper, PeopleService peopleService, FamilyService familyService, EmailService emailService) {
        this.personMapper = personMapper;
        this.familyMapper = familyMapper;
        this.peopleService = peopleService;
        this.familyService = familyService;
        this.emailService = emailService;
    }

    /**
     * Метод выполняет регистрацию пользователя {@link Person} и создание семьи {@link Family}.
     *
     * @param registrationRequestWithCreateFamily    Запрос на регистрацию с созданием семьи ({@link RegistrationRequestWithCreateFamily}).
     * @return                                      Ответ с информацией о семье ({@link RegistrationResponseWithCreateFamily}).
     */
    @Transactional
    public RegistrationResponseWithCreateFamily performRegistrationAndCreateFamily(RegistrationRequestWithCreateFamily registrationRequestWithCreateFamily) {
        Person person = personMapper.convertToPerson(registrationRequestWithCreateFamily.getPersonDTO());
        Family family = familyMapper.convertToFamily(registrationRequestWithCreateFamily.getFamilyDTO());

        person.setFamily(family);

        familyService.createFamilyAndAssignFamilyIdentifier(family);
        peopleService.createPersonWithActivationToken(person);

        emailService.sendActivationEmail(person.getEmail(), person.getActivationToken().getToken().toString());

        return new RegistrationResponseWithCreateFamily(family.getFamilyIdentifier());
    }

    /**
     * Метод выполняет регистрацию пользователя {@link Person} и его присоединение к семье {@link Family}.
     *
     * @param registrationRequestJoinFamily    Запрос на регистрацию с присоединением к семье ({@link RegistrationRequestJoinFamily}).
     * @throws FamilyNotFoundException Если семья сданным идентификатором не найдена в БД.
     */
    @Transactional
    public void performRegistrationAndJoinTheFamily(RegistrationRequestJoinFamily registrationRequestJoinFamily) {
        Person person = personMapper.convertToPerson(registrationRequestJoinFamily.getPersonDTO());
        String familyIdentifier = registrationRequestJoinFamily.getFamilyIdentifierDTO().getFamilyIdentifier();

        Family family = familyService.findByFamilyIdentifier(familyIdentifier).orElseThrow(() -> new FamilyNotFoundException(
                "Family with \"family identifier\" = " + familyIdentifier + " not found."
        ));

        person.setFamily(family);
        peopleService.createPersonWithActivationToken(person);

        emailService.sendActivationEmail(person.getEmail(), person.getActivationToken().getToken().toString());
    }
}
