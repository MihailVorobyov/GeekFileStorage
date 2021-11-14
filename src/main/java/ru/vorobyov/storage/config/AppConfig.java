package ru.vorobyov.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

import java.util.Properties;

@Configuration
public class AppConfig {
	@Value("${spring.datasource.url}")
	String url;

	@Value("${spring.datasource.username}")
	String username;

	@Value("${spring.datasource.password}")
	String password;

	@Bean
	public Sql2o sql2o() {
		return new Sql2o(url, username, password);
	}
	
}
