package com.semeinik.SemeinikRESTApp.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Сущность, представляющая токен активации для подтверждения аккаунта пользователя.
 *
 * Этот класс является сущностью JPA (Java Persistence API) и представляет таблицу "Activation_Token"
 * в базе данных. Он содержит информацию о токене активации, его создании, сроке действия и связанного пользователя.
 * Имеет связь с {@link Person} 1:1.
 * @author Denis Kolesnikov
 * @version 1.0
 *
 */
@Entity
@Table(name = "Activation_Token")
public class ActivationToken implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "token")
    private UUID token;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;
    @OneToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public ActivationToken() {
    }

    /**
     * Конструктор для создания экземпляра ActivationToken с указанием значений всех полей.
     *
     * @param token      Уникальный токен активации.
     * @param createdAt  Дата и время создания токена.
     * @param expiresAt  Дата и время истечения срока действия токена.
     * @param person     Пользователь ({@link Person}), связанный с этим токеном.
     */
    public ActivationToken(UUID token, ZonedDateTime createdAt, ZonedDateTime expiresAt, Person person) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID link) {
        this.token = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
