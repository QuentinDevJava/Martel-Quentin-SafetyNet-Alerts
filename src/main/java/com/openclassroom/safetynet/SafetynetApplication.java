package com.openclassroom.safetynet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.File;

/**
 * Main application class for the Safetynet application.
 * 
 * This class is responsible for starting the Spring Boot application and
 * checking for the presence of the "data.json" file in the classpath. If the
 * file is found, the application starts normally. If the file is not found, an
 * error message is logged and the application exits.
 */

@SpringBootApplication
@Slf4j
public class SafetynetApplication implements CommandLineRunner {

	/**
	 * Main method to start the Spring Boot application.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SafetynetApplication.class, args);
	}

	/**
	 * Runs a command after the application has started.
	 * 
	 * This method checks for the presence of the "data.json" file in the
	 * {@link ClassPathResource} . If the file is found, a debug message is logged.
	 * If the file is not found, an error message is logged and the application
	 * exits.
	 * 
	 * @param args Command line arguments.
	 */
	@Override
	public void run(String... args) {
		Resource resource = new ClassPathResource("data.json");
		if (resource.exists()) {
			log.debug("File 'data.json' found in the classpath. Starting the application.");
		} else {
			log.error("File 'data.json' not found in the classpath. Stopping the application.");
			System.exit(1);
		}
	}

}
