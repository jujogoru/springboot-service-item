package com.jujogoru.springboot.app.item.models.service;

import java.util.List;

import com.jujogoru.springboot.app.item.models.Item;
import com.jujogoru.springboot.app.commons.models.entity.Product;

public interface ItemService {
	
	public List<Item> findAll();
	public Item findById(Long id, Integer quantity);
	
	public Product save(Product product);
	public Product update(Product product, Long id);
	public void delete(Long id);
}
