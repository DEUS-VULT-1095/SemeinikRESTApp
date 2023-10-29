package com.semeinik.SemeinikRESTApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс, представляющий сущность пользователя в системе.
 *
 * Этот класс представляет собой сущность пользователя и содержит информацию о члене семьи.
 * Каждый поьзователь имеет уникальный идентификатор, электронную почту, имя, дату рождения,
 * роль в семье ({@link FamilyRole}), пароль, соль (для безопасности), роль пользователя, статус активации,
 * ссылку на семью ({@link Family}), ссесии ({@link Session}) и токен активации ({@link ActivationToken}).
 * Имеет связи: M:1 с {@link Family}, 1:M с {@link Session}, 1:1 с {@link ActivationToken}.
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email")
    @Pattern( regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Поле должно соответсвовать email формату")
    private String email;
    @Column(name = "name")
//    @NotEmpty(message = "Имя не должно быть пустым.")
//    @NotNull(message = "Имя не должно быть пустым.")
//    @Pattern(regexp = "^[A-Za-zА-Яа-я-]{3,30}$", message = "Имя должно состоять из латинских или русских букв." +
//            " Не содержать пробелы. Может содержать символ дефиса \"-\". От 3-х до 30-ти символов включительно.")
    private String name;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "family_role")
    @Enumerated(EnumType.STRING)
    private FamilyRole familyRole; // Семейная роль пользователя в семье (брат, отец, сестра...).
    @Column(name = "password")
    private String password;
    @Column(name = "salt")
    private String salt; // Соль.
    @Column(name = "role")
    private String role; // Роль пользователя в системе (USER, ADMIN ...).
    @Column(name = "is_activated")
    private boolean isActivated; // Флаг активации аккаунта.
    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "id")
    private Family family; // Семья, в которой состоит пользователь.
    @OneToMany(mappedBy = "person")
    private List<Session> sessions; // Сессии пользователя.
    @OneToOne(mappedBy = "person")
    private ActivationToken activationToken; // Активационный токен для аккаунта.

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public FamilyRole getFamilyRole() {
        return familyRole;
    }

    public void setFamilyRole(FamilyRole familyRole) {
        this.familyRole = familyRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public ActivationToken getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(ActivationToken activationToken) {
        this.activationToken = activationToken;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
