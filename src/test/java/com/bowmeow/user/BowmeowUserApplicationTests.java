package com.bowmeow.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class BowmeowUserApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class TestConfig {
		// 필요한 경우 테스트에 필요한 빈을 여기서 정의할 수 있습니다.
	}

}
