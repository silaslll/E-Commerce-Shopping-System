package com.shopme.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	@Autowired
	private CustomerService service;

	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
		if (service.isEmailUnique(id, email)) {
			return "OK";
		} else {
			return "Duplicated";
		}
	}
}