package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)        
public class RoleRepositoryTests {

	
	/*
	 * In this test class, we need to have a reference to the 
	 * RoleRepository with the @Autowired annotation to let 
	 * Spring framework inject an instance of the RoleRepository 
	 * interface. 
	 * */
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin" , "manage everything");
		Role savedRole = repo.save(roleAdmin);   //save() returns a persistent Role object
		assertThat(savedRole.getId()).isGreaterThan(0);   //meaning that the Role object has been actually persisted into the database
		
	}
	
	@Test
	public void testCreateRestRole() {
		Role roleSalesperson = new Role("Salesperson" , "manage product price,"
				+ " customers, shipping, orders and sales report");
		Role roleEditor = new Role("Editor" , "manage categories, brands,"
				+ " products, articles and menus");
		Role roleShipper = new Role("Shipper" , "view products, view orders, "
				+ "and update order status");
		Role roleAssistant = new Role("Assistant" , "manage questions and reviews");
		
		repo.saveAll(List.of(roleSalesperson , roleEditor, roleShipper , roleAssistant));
				
	 
		
	}
	
	
	
	
	
}
