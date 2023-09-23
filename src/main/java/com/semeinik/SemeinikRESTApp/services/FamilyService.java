package com.semeinik.SemeinikRESTApp.services;

import com.semeinik.SemeinikRESTApp.exceptions.FamilyNotFoundException;
import com.semeinik.SemeinikRESTApp.exceptions.PersonNotFoundException;
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
 *
 * Этот класс предоставляет методы для управления семейными данными, такими как создание новой семьи,
 * удаление семьи и поиск семьи по идентификатору семьи.
 * @Transactional(readOnly = true) Эта аннотация означает, что все методы этого класса будут выполняться внутри транзакций
 * только для чтения. Если метод выполняет не только чтение, нужно пометить его аннотацией {@code @Transactional}.
 *
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final PeopleService peopleService;

    /**
     * Конструктор класса сервиса для работы с семьёй.
     *
     * @param familyRepository Репозиторий для работы с данными семей ({@link  FamilyRepository}).
     * @param peopleService Сервис для работы с пользователями ({@link PeopleService}).
     */
    @Autowired
    public FamilyService(FamilyRepository familyRepository, PeopleService peopleService) {
        this.familyRepository = familyRepository;
        this.peopleService = peopleService;
    }

    /**
     * Создание новой семьи ({@link Family}) и сохранение ее в БД.
     *
     * @param family Семья ({@link Family}) для создания и сохранения.
     */
    @Transactional
    public void createFamilyAndAssignFamilyIdentifier(Family family) {
        String familyIdentifier = IdentifierFamilyGenerator.generateFamilyIdentifier();
        family.setFamilyIdentifier(familyIdentifier);
        familyRepository.save(family);
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
    public void deleteFamilyFromPerson() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Person person = peopleService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new PersonNotFoundException("Person with email \"" + userDetails.getUsername() + "\" not found"));

        if (person.getFamily() == null) {
            throw new FamilyNotFoundException("Person with email \"" + person.getEmail() + "\" have not family");
        }

        familyRepository.delete(person.getFamily());
    }
}
