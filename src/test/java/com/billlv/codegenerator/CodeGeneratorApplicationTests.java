package com.billlv.codegenerator;

import com.billlv.codegenerator.domain.entity.Menu;
import com.billlv.codegenerator.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeGeneratorApplicationTests {
	@Autowired
	private MenuRepository menuRepository;
	@Test
	void contextLoads() {
		Menu user = new Menu();
		user.setCode("test_user");
		user.setName("test@example.com");

		// 保存用户
		Menu menu = menuRepository.save(user);

		// 验证审计字段是否自动赋值
		System.out.println("Created At: " + menu.getCreatedAt());
		System.out.println("Created By: " + menu.getCreatedBy());
		System.out.println("Updated At: " + menu.getUpdatedAt());
		System.out.println("Updated By: " + menu.getUpdatedBy());
	}
}