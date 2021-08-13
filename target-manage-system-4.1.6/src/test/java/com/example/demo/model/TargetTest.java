package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class TargetTest {

	@Test
	@DisplayName("targetクラスのequalsメゾットのテスト")
	void whenTargetEquals_returnTrue() {
		//		Arrange
		Target target1 = new Target();
		target1.setId((long) 1);

		Target target2 = new Target();
		target2.setId((long) 2);

		Target target3 = new Target();
		target3.setId((long) 1);

		//		Act
		boolean result1 = target1.equals(target2);
		boolean result2 = target1.equals(target3);
		boolean result3 = target1.equals(target1);

		//		Assert
		assertFalse(result1);
		assertTrue(result2);
		assertTrue(result3);

		System.out.println(target1.getKind().toString());
	}

}
