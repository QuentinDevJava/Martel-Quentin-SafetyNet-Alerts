package com.openclassroom.safetynet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SafetynetApplicationTests {

	@Test
	void teststd() {
		int a=4;
		int b=2;
		int result;
		result=a*b;
		assertThat(result).isEqualTo(8);
		
	}

}
