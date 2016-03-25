package com.example;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.controller.ValantController;
import com.example.model.InventoryRepository;
import com.example.model.Item;

@SpringBootApplication
public class MyjpaApplication {
	static Logger logger = Logger.getLogger(MyjpaApplication.class.getName());
	public static Date getExpirationDate(int days) {
		Calendar cday = Calendar.getInstance();
		cday.add(Calendar.DATE, days); 
		return cday.getTime();
	}
	/**
	 * Seed items into the InventoryRepository
	 */
	@Bean
	public InitializingBean seedDB(InventoryRepository ir) {
		return ()-> {
			ir.save(new Item("Label1", getExpirationDate(2), "Type_1"));
			ir.save(new Item("Label2", getExpirationDate(10), "Type_2"));
			ir.save(new Item("Label3", getExpirationDate(4), "Type_3"));
		};
	}
	/**
	 * Display the seeded items, just for verification purpose
	 */
	@Bean
	public CommandLineRunner showDbItem(InventoryRepository repository) {
		return args -> {
			repository.findAll().forEach( System.out::println); };
			//TODO: want to print to logger, but get syntax error? 
			//repository.findAll().forEach( (i)-> {logger.info(i.toString());} );
	}

	public static void main(String[] args) {
		SpringApplication.run(MyjpaApplication.class, args);
	}
}
