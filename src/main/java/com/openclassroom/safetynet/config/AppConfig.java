package com.openclassroom.safetynet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class for the application.
 * 
 * This class defines a bean for the ObjectMapper, which is used for JSON
 * serialization and deserialization.
 * 
 */
@Configuration
public class AppConfig {
	/**
	 * Creates a new ObjectMapper bean.
	 * 
	 * @return A new ObjectMapper instance.
	 */
	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
