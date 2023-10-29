package com.semeinik.SemeinikRESTApp.mappers;

import com.semeinik.SemeinikRESTApp.dto.PersonDTO;
import com.semeinik.SemeinikRESTApp.models.FamilyRole;
import com.semeinik.SemeinikRESTApp.models.Person;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonMapperTests {
    private String email;
    private String name;
    private LocalDate dateOfBirth;
    private FamilyRole familyRole;
    private String password;
    private String confirmPassword;
    @Autowired
    private PersonMapper personMapper;

    @BeforeEach
    void beforeEach() {
        email = "test@test.com";
        name = "testName";
        dateOfBirth = LocalDate.of(1992, 7, 1);
        familyRole = FamilyRole.FATHER;
        password = "password";
        confirmPassword = "password";
    }

    @DisplayName("Test convert PersonDTO to Person")
    @Test
    void testConvertToPerson_whenProvidedPersonDTO_returnsPerson() {
        // Arrange
        PersonDTO personDTO = new PersonDTO(email, name, dateOfBirth, familyRole, password);

        // Act
        Person person = personMapper.convertToPerson(personDTO);

        // Assert
        assertEquals(email, person.getEmail(), "Should returns correct email");
        assertEquals(name, person.getName(), "Should returns correct name");
        assertEquals(dateOfBirth, person.getDateOfBirth(), "Should returns correct date of birth");
        assertEquals(familyRole, person.getFamilyRole(), "Should returns correct family role");
        assertEquals(password, person.getPassword(), "Should returns correct password");
    }

    @DisplayName("Test convert Person to PersonDTO")
    @Test
    void testConvertToPersonDTO_whenProvidedPerson_returnsPersonDTO() {
        // Arrange
        Person person = new Person();
        person.setEmail(email);
        person.setName(name);
        person.setDateOfBirth(dateOfBirth);
        person.setFamilyRole(familyRole);
        person.setPassword(password);

        // Act
        PersonDTO personDTO = personMapper.convertToPersonDTO(person);

        // Assert
        assertEquals(email, personDTO.getEmail(), "Should returns correct email");
        assertEquals(name, personDTO.getName(), "Should returns correct name");
        assertEquals(dateOfBirth, personDTO.getDateOfBirth(), "Should returns correct date of birth");
        assertEquals(familyRole, personDTO.getFamilyRole(), "Should returns correct family role");
        assertEquals(password, personDTO.getPassword(), "Should returns correct password");
    }
}
