package com.openclassroom.safetynet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class SafetynetApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SafetynetApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ClassPathResource jsonDataFileRessource = new ClassPathResource("data.json");
		if (jsonDataFileRessource.exists()) {
			log.debug("File 'data.json' found in the classpath. Starting the application.");
		} else {
			log.error("File 'data.json' not found in the classpath. Stopping the application.");
			System.exit(1);
		}
	}

}
