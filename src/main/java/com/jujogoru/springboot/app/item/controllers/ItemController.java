package com.jujogoru.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jujogoru.springboot.app.item.models.Item;
import com.jujogoru.springboot.app.commons.models.entity.Product;
import com.jujogoru.springboot.app.item.models.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RefreshScope
@RestController
public class ItemController {
	
	private static Logger log = LoggerFactory.getLogger(ItemController.class);
	
	private static String DEV_ENVIRONMENT = "dev";
	
	@Autowired
	private Environment environment;
	
	@Autowired
	@Qualifier("serviceFeign")
//	@Qualifier("serviceRestTemplate")
	private ItemService itemService;
	
	@Value("${configuration.text}")
	private String propertiesText; 
	
	@GetMapping("/")
	public List<Item> list(){
		return itemService.findAll();
	}
	
	@HystrixCommand(fallbackMethod =  "alternativeMethod")
	@GetMapping("/{id}/quantity/{quantity}")
	public Item detail(@PathVariable Long id, @PathVariable Integer quantity) {
		
		//TODO Implement a 404 response when there's no product found
		
		return itemService.findById(id, quantity);
	}
	
	public Item alternativeMethod(@PathVariable Long id, @PathVariable Integer quantity) {
		Item item = new Item();
		Product product = new Product();
		
		item.setQuantity(quantity);
		product.setId(id);
		product.setName("Dummy name");
		product.setPrice(666.666);
		item.setProduct(product);
		return item;
	}
	
	@GetMapping("/config")
	public ResponseEntity<?> getConfig(@Value("${server.port}") String port){
		
		log.info("Custom text: " + propertiesText);
		
		Map<String, String> json = new HashMap<>();
		json.put("propertiesText", propertiesText);
		json.put("port", port);
		
		if(environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals(DEV_ENVIRONMENT)) {
			json.put("author.name", environment.getProperty("configuration.author.name"));
			json.put("author.email", environment.getProperty("configuration.author.email"));
		}
		
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
	}
	
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public Product create(@RequestBody Product product) {
		return itemService.save(product);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Product update(@RequestBody Product product, @PathVariable Long id) {
		return itemService.update(product, id);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		itemService.delete(id);
	}
}
