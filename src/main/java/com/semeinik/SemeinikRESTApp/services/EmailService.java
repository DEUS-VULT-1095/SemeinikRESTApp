package com.semeinik.SemeinikRESTApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.semeinik.SemeinikRESTApp.models.ActivationToken;

/**
 * Класс сервиса для отправки электронных писем.
 *
 * Этот класс предоставляет методы для отправки электронных писем, таких как письма активации учетных записей.
 *
 * @author Denis Koldesnikov
 * @version 1.0
 */
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    /**
     * Конструктор класса, принимающий {@link JavaMailSender} в качестве зависимости.
     *
     * @param javaMailSender Объект {@link JavaMailSender} для отправки писем.
     */
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Отправка письма активации учетной записи на указанный адрес.
     *
     * @param to              Адрес электронной почты, на который отправляется письмо активации.
     * @param activationToken Токен активации, включенный в письмо.
     * @see ActivationToken
     */
    public void sendActivationEmail(String to, String activationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Activate Your Account");
        message.setText("Click the link below to activate your account:\n" + "http://localhost:8080/activate/" + activationToken);
        javaMailSender.send(message);
    }
}
