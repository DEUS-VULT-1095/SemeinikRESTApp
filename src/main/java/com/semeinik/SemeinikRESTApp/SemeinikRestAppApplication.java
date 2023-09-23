package com.semeinik.SemeinikRESTApp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;
import java.util.Random;

/**
 * Главный класс приложения SemeinikRestAppApplication.
 * Этот класс является точкой входа для запуска Spring Boot приложения SemeinikRestAppApplication.
 * Он также содержит конфигурацию и создает бин (bean) ModelMapper для маппинга объектов.
 * @author Denis Kolesnikov
 * @version 1.0
 */
@SpringBootApplication
public class SemeinikRestAppApplication {

	/**
	 * Метод main, используемый для запуска Spring Boot приложения.
	 *
	 * @param args Аргументы командной строки.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SemeinikRestAppApplication.class, args);
	}

	/**
	 * Создает и возвращает экземпляр бина (bean) ModelMapper.
	 * ModelMapper - это инструмент для маппинга объектов между разными классами.
	 *
	 * @return Экземпляр ModelMapper.
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
