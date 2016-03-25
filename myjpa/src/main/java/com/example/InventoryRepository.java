package com.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface InventoryRepository extends CrudRepository<Item, Long> {

	@RestResource(path="find")
	String findByLabelIgnoreCase(@Param("label") String label);
}
