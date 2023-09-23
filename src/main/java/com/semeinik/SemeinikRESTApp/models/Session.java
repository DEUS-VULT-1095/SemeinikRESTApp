package com.semeinik.SemeinikRESTApp.models;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Класс, представляющий сущность "Сессия" в системе.
 *
 * Этот класс представляет собой сущность "Сессия" и содержит информацию о сессии пользователя.
 * Каждая сессия имеет уникальный идентификатор, токен обновления (refresh token), время истечения,
 * дату создания и ссылку на пользователя, к которому относится сессия.
 * Имеет связь M:1 с {@link Person}.
 * @author Denis Kolesnikov
 * @version 1.0
 */
@Entity
@Table(name = "Session")
public class Session {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "refreshToken")
    private UUID refreshToken;
    @Column(name = "expiresIn")
    private ZonedDateTime expiresIn;
    @Column(name = "createdAt")
    private ZonedDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "personId", referencedColumnName = "id")
    private Person person;

    public Session() {
    }

    /**
     * Конструктор класса Session с параметрами.
     *
     * @param refreshToken Токен обновления сессии (refresh token).
     * @param expiresIn    Время истечения сессии.
     * @param createdAt    Дата создания сессии.
     * @param person       Пользователь, к которому относится сессия ({@link Person}).
     */
    public Session(UUID refreshToken, ZonedDateTime expiresIn, ZonedDateTime createdAt, Person person) {
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.createdAt = createdAt;
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(UUID refreshToken) {
        this.refreshToken = refreshToken;
    }

    public ZonedDateTime getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(ZonedDateTime expiresIn) {
        this.expiresIn = expiresIn;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
