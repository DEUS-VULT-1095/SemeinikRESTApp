package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.dto.FamilyDTO;
import com.semeinik.SemeinikRESTApp.exceptions.FamilyNotCreatedException;
import com.semeinik.SemeinikRESTApp.exceptions.FamilyNotFoundException;
import com.semeinik.SemeinikRESTApp.exceptions.PersonNotFoundException;
import com.semeinik.SemeinikRESTApp.mappers.FamilyMapper;
import com.semeinik.SemeinikRESTApp.models.Family;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.repositories.FamilyRepository;
import com.semeinik.SemeinikRESTApp.utils.IdentifierFamilyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Класс сервиса для работы с семейными данными.
 * <p>
 * Этот класс предоставляет методы для управления семейными данными, такими как создание новой семьи,
 * удаление семьи и поиск семьи по идентификатору семьи.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 * @Transactional(readOnly = true) Эта аннотация означает, что все методы этого класса будут выполняться внутри транзакций
 * только для чтения. Если метод выполняет не только чтение, нужно пометить его аннотацией {@code @Transactional}.
 */
@Service
@Transactional(readOnly = true)
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final PeopleService peopleService;
    private final FamilyMapper familyMapper;

    /**
     * Конструктор класса сервиса для работы с семьёй.
     *
     * @param familyRepository Репозиторий для работы с данными семей ({@link  FamilyRepository}).
     * @param peopleService    Сервис для работы с пользователями ({@link PeopleService}).
     * @param familyMapper     Утилита для конвертации {@link Family} в {@link FamilyDTO} и обратно.
     */
    @Autowired
    public FamilyService(FamilyRepository familyRepository, PeopleService peopleService, FamilyMapper familyMapper) {
        this.familyRepository = familyRepository;
        this.peopleService = peopleService;
        this.familyMapper = familyMapper;
    }

    /**
     * Создание новой семьи ({@link Family}) и сохранение ее в БД.
     *
     * @param familyDTO DTO {@link FamilyDTO} сущности {@link Family}.
     * @return Возвращает идентификатор созданной семьи.
     */
    @Transactional
    public String createFamilyAndSave(FamilyDTO familyDTO, UserDetails userDetails) {
        Person person = peopleService
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new PersonNotFoundException("Person with email \"" + userDetails.getUsername() + "\" not found"));

        if (person.getFamily() != null) {
            throw new FamilyNotCreatedException("You already have a family");
        }

        Family family = familyMapper.convertToFamily(familyDTO);
        String familyIdentifier;

        do {
            familyIdentifier = IdentifierFamilyGenerator.generateFamilyIdentifier();
        } while (findByFamilyIdentifier(familyIdentifier).isPresent());

        family.setFamilyIdentifier(familyIdentifier);
        familyRepository.save(family);
        person.setFamily(family);

        return familyIdentifier;
    }

    /**
     * Удаление указанной семьи ({@link Family}) из БД.
     *
     * @param family Семья ({@link Family}) для удаления.
     */
    @Transactional
    public void delete(Family family) {
        familyRepository.delete(family);
    }

    /**
     * Поиск семьи ({@link Family}) по идентификатору семьи.
     *
     * @param familyIdentifier Идентификатор семьи для поиска.
     * @return Семья ({@link Family}), соответствующая указанному идентификатору, если она существует.
     */
    public Optional<Family> findByFamilyIdentifier(String familyIdentifier) {
        return familyRepository.findByFamilyIdentifier(familyIdentifier);
    }

    /**
     * Метод удаляет семью у существующего пользователя.
     *
     * @throws PersonNotFoundException Если пользователь {@link Person} не найден в БД.
     * @throws FamilyNotFoundException Если у пользователя {@link Person} нет семьи {@link Family}.
     */
    @Transactional
    public void deleteFamilyFromPerson() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Person person = peopleService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new PersonNotFoundException("Person with email '" + userDetails.getUsername() + "' not found"));

        if (person.getFamily() == null) {
            throw new FamilyNotFoundException("Person with email '" + person.getEmail() + "' have not family");
        }

        familyRepository.delete(person.getFamily());
    }

    /**
     * Присоединяет пользователя к существующей семье.
     *
     * @param familyIdentifier Идентификатор семьи ({@link Family}).
     * @param userDetails      Объект, содержащие данные аутентификации пользователя.
     */
    @Transactional
    public void joinFamily(String familyIdentifier, UserDetails userDetails) {
        Family family = findByFamilyIdentifier(familyIdentifier).
                orElseThrow(() -> new FamilyNotFoundException("Family with '" + familyIdentifier + "' identifier not found"));

        Person person = peopleService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new PersonNotFoundException("Person with email '" + userDetails.getUsername() + "' not found"));

        person.setFamily(family);
        family.getPeople().add(person);
    }
}
