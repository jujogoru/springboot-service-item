package com.jujogoru.springboot.app.item.models;

import com.jujogoru.springboot.app.commons.models.entity.Product;

public class Item {

	Product product;
	private Integer quantity;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Item(Product product, Integer quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}

	public Item() {
	}
	
	public Double getTotal() {
		return product.getPrice() * quantity.doubleValue();
	}
}
