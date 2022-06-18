package com.shopme.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

@Service
public class AddressService {

	@Autowired private AddressRepository repo;

	public List<Address> listAddressBook(Customer customer) {
		return repo.findByCustomer(customer);
	}

}