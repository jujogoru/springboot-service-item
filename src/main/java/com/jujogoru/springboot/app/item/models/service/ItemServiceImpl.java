package com.jujogoru.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jujogoru.springboot.app.item.models.Item;
import com.jujogoru.springboot.app.commons.models.entity.Product;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements ItemService {

	@Autowired
	private RestTemplate restClient;
	
	@Override
	public List<Item> findAll() {
		List<Product> products = Arrays.asList(restClient.getForObject("http://service-products/", Product[].class));
		return products.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer quantity) {		
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		Product product = restClient.getForObject("http://service-products/{id}", Product.class, pathVariables);
		return new Item(product, quantity);
	}

	@Override
	public Product save(Product product) {
		HttpEntity<Product> body = new HttpEntity<Product>(product);
		ResponseEntity<Product> response = restClient.exchange("http://service-products/", HttpMethod.POST, body, Product.class);
		Product productResponse = response.getBody();
		return productResponse;
	}

	@Override
	public Product update(Product product, Long id) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		
		HttpEntity<Product> body = new HttpEntity<Product>(product);
		ResponseEntity<Product> response = restClient.exchange("http://service-products/{id}", 
				HttpMethod.PUT, body, Product.class, pathVariables);
		return response.getBody();
	}

	@Override
	public void delete(Long id) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		restClient.delete("http://service-products/{id}", pathVariables);
		
	}
}
