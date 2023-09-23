package com.semeinik.SemeinikRESTApp.controllers;

import com.semeinik.SemeinikRESTApp.dto.FamilyDTO;
import com.semeinik.SemeinikRESTApp.dto.FamilyIdentifierDTO;
import com.semeinik.SemeinikRESTApp.exceptions.FamilyNotCreatedException;
import com.semeinik.SemeinikRESTApp.exceptions.FamilyNotFoundException;
import com.semeinik.SemeinikRESTApp.exceptions.PersonNotFoundException;
import com.semeinik.SemeinikRESTApp.models.Family;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.services.FamilyService;
import com.semeinik.SemeinikRESTApp.services.PeopleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Класс контроллера для управления семьями.
 *
 * Этот класс предоставляет API для создания, удаления и проверки семей.
 *
 * @RestController Указывает, что этот класс является контроллером REST API.
 * @RequestMapping("/family") Определяет базовый URI для всех методов контроллера, начинающихся с "/family".
 * @author Denis Kolesnikov
 * @version 1.0
 */
@RestController
@RequestMapping("/family")
public class FamilyController {
    private final FamilyService familyService; // Сервис для работы с семьями.

    /**
     * Конструктор контроллера FamilyController.
     *
     * @param familyService Сервис для работы с семьями ({@link FamilyService}).
     */
    @Autowired
    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    /**
     * Создает новую семью и связывает ее с текущим пользователем.
     *
     * @param familyDTO DTO с данными семьи для создания ({@link FamilyDTO}).
     * @param bindingResult Результаты проверки входных данных.
     * @return ResponseEntity, включающий Map с ключом "familyIdentifier" и значением идентификатора семьи, и статусом HttpStatus.OK.
     * @throws FamilyNotCreatedException если поля экземпляра класса {@link FamilyDTO} не прошли валидацию или у пользователь уже состоит в семье.
     * @throws PersonNotFoundException если пользователя нет в БД.
     */
//    @PostMapping("/add")
//    public ResponseEntity<?> createFamily(@RequestBody @Valid FamilyDTO familyDTO, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            throw new FamilyNotCreatedException(createErrorMsg(bindingResult));
//        }
//
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        Optional<Person> potentialPerson = peopleService.findByEmail(userDetails.getUsername());
//
//        if (potentialPerson.isEmpty()) {
//            throw new PersonNotFoundException("Person with email \"" + userDetails.getUsername() + "\" not found");
//        }
//
//        Person person = potentialPerson.get();
//
//        if (person.getFamily() != null) {
//            throw new FamilyNotCreatedException("You already have a family");
//        }
//
//        Family family = convertToFamily(familyDTO);
//        person.setFamily(family);
//        String familyIdentifier = familyService.save(family);
//
//        return new ResponseEntity<>(Map.of("familyIdentifier", familyIdentifier), HttpStatus.OK);
//    }

    /**
     * Удаляет семью текущего пользователя.
     *
     * @return ResponseEntity со статусом HttpStatus.NO_CONTENT.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteFamily() {
        familyService.deleteFamilyFromPerson();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Проверяет наличие семьи ({@link Family}) по ее идентификатору.
     *
     * @param familyIdentifierDTO DTO (Data Transfer Object) с идентификатором семьи для проверки ({@link FamilyIdentifierDTO}).
     * @return ResponseEntity с булевым значением проверки наличия семьи ({@link Family}) и со статусом HttpStatus.OK.
     */
    @GetMapping("/exist-family-identifier")
    public ResponseEntity<Boolean> existFamilyIdentifier(@RequestBody FamilyIdentifierDTO familyIdentifierDTO) {
        return ResponseEntity.ok(familyService.findByFamilyIdentifier(familyIdentifierDTO.getFamilyIdentifier()).isPresent());
    }
}
