package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity                //Hibernate will create table from Entity class
@Table(name = "roles") // use @Table with the name equal to the actual name of the table in the database: roles.
public class Role extends IdBasedEntity {
	/*
	 * In this entity class, we need to declare the instance fields that map to the
	 * corresponding columns in the database table.
	 */

	
	/*Map the fields to corresponding columns in the Roles table*/
	
	/*@GeneratedValue annotation tells Hibernate that and the values 
	 * of this field will be generated automatically. 
	 * With the strategy GenerationType.IDENTITY
	 */
		
	

	/*@Column means this field name has same name with 
	 * the column in the database. 
	 */
	@Column(length = 40, nullable = false, unique = true)
	private String name;
	
	@Column(length = 150, nullable = false)
	private String description;

	public Role() {
	}
	
	public Role(Integer id) {
		this.id = id;
	}

	public Role(String name) {
		this.name = name;
	}	
	
	public Role(String name, String description) {
		this.name = name;
		this.description = description;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	
}
