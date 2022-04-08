package com.shopme.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Role;  //before importing, add the shopmecommon dependency in to the webparent pom.xml 



@Repository
public interface RoleRepository extends CrudRepository<Role,Integer>{  // the id type is Integer

	
	
}
