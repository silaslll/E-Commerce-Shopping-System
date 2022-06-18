package com.shopme.common.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.entity.IdBasedEntity;

@Entity
@Table(name = "product_images")
public class ProductImage extends IdBasedEntity {
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductImage() {
	}

	public ProductImage(Integer id, String name, Product product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}


	public ProductImage(String name, Product product) {
		this.name = name;
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Transient
	public String getImagePath() {
		return "/product-images/" + product.getId() + "/extras/" + this.name;
	}
	
}