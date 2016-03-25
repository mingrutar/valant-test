package com.example.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.example.services.Notificator;

@Repository
public interface InventoryRepository extends CrudRepository<Item, Long> {

//	@RestResource(path="find")
	Optional<Item> findByLabelIgnoreCase(@Param("label") String label);

}
