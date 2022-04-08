package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles") // use @Table with the name equal to the actual name of the table in the
						// database: roles.
public class Role {

	/*
	 * In this entity class, we need to declare the instance fields that map to the
	 * corresponding columns in the database table.
	 */

	
	/*Map the fields to corresponding columns in the Roles table*/
	
	/*@GeneratedValue annotation tells Hibernate that and the values 
	 * of this field will be generated automatically. 
	 * With the strategy GenerationType.IDENTITY
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //
	private Integer id;
	
	

	/*@Column means this field name has same name with 
	 * the column in the database. 
	 */
	@Column(length = 40 , nullable = false , unique = true)
	private String name;
	
	
	@Column(length = 150 , nullable = false)
	private String description;
	
	
	
	public Role() {	
	}
	
	public Role(String name) {
		super();
		this.name = name;
	}
	
	
	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
