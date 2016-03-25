package com.example;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyjpaApplication {
	
	private Date getExpirationDate(int days) {
		Calendar cday = Calendar.getInstance();
		cday.add(Calendar.DATE, days); 
		return cday.getTime();
	}
	
	@Bean
	public InitializingBean seedDB(InventoryRepository ir) {
		return ()-> {
			ir.save(new Item("Label1", getExpirationDate(2), "Type_1"));
			ir.save(new Item("Label2", getExpirationDate(10), "Type_2"));
			ir.save(new Item("Label3", getExpirationDate(4), "Type_3"));
		};
	}
	@Bean
	public CommandLineRunner showDbItem(InventoryRepository repository) {
		return args -> {
			repository.findAll().forEach(System.out::println); };
	}

	public static void main(String[] args) {
		SpringApplication.run(MyjpaApplication.class, args);
	}
}
