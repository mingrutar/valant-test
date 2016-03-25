package com.example;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface InventoryRepository extends CrudRepository<Item, Long> {

//	@RestResource(path="find")
	Optional<Item> findByLabelIgnoreCase(@Param("label") String label);
}
