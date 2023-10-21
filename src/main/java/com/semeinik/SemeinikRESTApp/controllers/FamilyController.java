package com.semeinik.SemeinikRESTApp.controllers;

import com.semeinik.SemeinikRESTApp.dto.FamilyDTO;
import com.semeinik.SemeinikRESTApp.exceptions.FamilyNotCreatedException;
import com.semeinik.SemeinikRESTApp.exceptions.PersonNotFoundException;
import com.semeinik.SemeinikRESTApp.models.Family;
import com.semeinik.SemeinikRESTApp.models.Person;
import com.semeinik.SemeinikRESTApp.services.FamilyService;
import com.semeinik.SemeinikRESTApp.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.semeinik.SemeinikRESTApp.utils.ErrorMsgCreator.createErrorMsg;


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
    /** Сервис для работы с семьями. */
    private final FamilyService familyService;
    /** Сервис для работы с пользователями. */
    private final PeopleService peopleService;

    /**
     * Конструктор контроллера FamilyController.
     *
     * @param familyService Сервис {@link FamilyService} для работы с семьями {@link Family}.
     * @param peopleService Сервис {@link PeopleService} для работы с пользователями {@link Person}.
     */
    @Autowired
    public FamilyController(FamilyService familyService, PeopleService peopleService) {
        this.familyService = familyService;
        this.peopleService = peopleService;
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
    @PostMapping("/create")
    public ResponseEntity<?> createFamily(@RequestBody @Valid FamilyDTO familyDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FamilyNotCreatedException(createErrorMsg(bindingResult));
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String familyIdentifier = familyService.createFamilyAndSave(familyDTO, userDetails);

        return new ResponseEntity<>(Map.of("familyIdentifier", familyIdentifier), HttpStatus.CREATED);
    }

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
     * @param familyIdentifier Идентификатор семьи.
     * @return ResponseEntity с булевым значением проверки наличия семьи ({@link Family}) и со статусом HttpStatus.OK.
     */
    @GetMapping("/exist-family-identifier")
    public ResponseEntity<Boolean> existFamilyIdentifier(@RequestParam String familyIdentifier) {
        return ResponseEntity.ok(familyService.findByFamilyIdentifier(familyIdentifier).isPresent());
    }
}
