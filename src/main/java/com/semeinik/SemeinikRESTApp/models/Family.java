package com.semeinik.SemeinikRESTApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая семейную группу в системе.
 *
 * Этот класс является сущностью JPA (Java Persistence API) и представляет таблицу "Family"
 * в базе данных. Он содержит информацию о семейной группе, включая ее идентификатор, имя и членов семьи.
 * Имеет связь 1:M с {@link Person}.
 * @version 1.0
 * @author Ваше имя
 */
@Entity
@Table(name = "Family")
public class Family {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "family_name")
    @NotNull(message = "Фамилия не должна быть пустой.")
    @NotEmpty(message = "Фамилия не должна быть пустой.")
    @Pattern(regexp = "^[A-Za-zА-Яа-я ]{3,30}$", message = "Фамилия должна состоять из латинских или русских букв." +
            "Не содержать пробелы. От 3-х до 30-ти символов включительно.")
    private String familyName;
    @Column(name = "family_identifier")
    private String familyIdentifier;
    @OneToMany(mappedBy = "family")
    private List<Person> people = new ArrayList<>();

    public Family() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public String getFamilyIdentifier() {
        return familyIdentifier;
    }

    public void setFamilyIdentifier(String familyIdentifier) {
        this.familyIdentifier = familyIdentifier;
    }
}
