package com.openclassroom.safetynet.config;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
