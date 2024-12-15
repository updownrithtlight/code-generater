package com.billlv.codegenerator;

import com.billlv.codegenerator.controller.MenuController;
import com.billlv.codegenerator.domain.entity.Menu;
import com.billlv.codegenerator.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.InputStream;
import java.util.List;

@SpringBootTest
class CodeGeneratorApplicationTests {
	@Autowired
	private MenuController menuController;
	@Test
	void contextLoads() {

		User mockUser = new User("test_user", "", List.of());
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				mockUser, null, mockUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Menu menu = new Menu();
		menu.setCode("test_user");
		menu.setName("test@example.com");

		// 保存用户
		menu = menuController.createMenu(menu);

		// 验证审计字段是否自动赋值
		System.out.println("Created At: " + menu.getCreatedAt());
		System.out.println("Created By: " + menu.getCreatedBy());
		System.out.println("Updated At: " + menu.getUpdatedAt());
		System.out.println("Updated By: " + menu.getUpdatedBy());
	}


	@Test
	public void testLoadPrivateKey() {
		try {
			Resource resource = new ClassPathResource("keys/private_key.pem");
			InputStream inputStream = resource.getInputStream();
			System.out.println("Private key content: " + new String(inputStream.readAllBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
