package com.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends CrudRepository<Item, Long> {
	String findByLabelIgnoreCase(@Param("label") String label);
}
