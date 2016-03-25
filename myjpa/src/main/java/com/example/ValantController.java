package com.example;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/")
public class ValantController {
	private InventoryRepository inventoryRepository;
	
	@Autowired
	ValantController(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}
		
	@RequestMapping(method = RequestMethod.GET)
	Collection<Item> allItems() {
		Collection<Item> ret = new ArrayList<Item>();
		Iterable<Item> iter = this.inventoryRepository.findAll();
		for (Item item : iter) {
			ret.add(item);
		}
		return ret;
	}
	
	/**
	 * curl -H "Content-Type: application/json" -X POST -d '{"label":"xyz","expiration":234567,"type":"A"}' http://localhost:8080/
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@RequestBody Item input) {
		Item result = this.inventoryRepository.save(input);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/")
				.buildAndExpand(result.getId()).toUri());
		return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
	}
	/*
	 * curl -i -H "Accept: application/json" -X DELETE http://localhost:8080/Label1 
	 */
	@RequestMapping(value = "/{itemLabel}", method = RequestMethod.DELETE)
	ResponseEntity<?> delete(@PathVariable String itemLabel) {
		Item item = this.inventoryRepository.findByLabelIgnoreCase(itemLabel).orElseThrow(()->new LabelNotFoundException(itemLabel));
		this.inventoryRepository.delete(item);

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>(null, httpHeaders, HttpStatus.NO_CONTENT);
	}
	
}
@ResponseStatus(HttpStatus.NOT_FOUND)
class LabelNotFoundException extends RuntimeException {

	public LabelNotFoundException(String label) {
		super("could not find label '" + label + "'.");
	}
}
