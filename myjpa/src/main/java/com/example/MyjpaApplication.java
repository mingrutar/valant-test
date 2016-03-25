package com.example;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.controller.ValantController;
import com.example.model.InventoryRepository;
import com.example.model.Item;
import com.example.services.Notificator;
import com.example.services.SimpleReceiver;

import reactor.Environment;
import reactor.bus.EventBus;

import static reactor.bus.selector.Selectors.$;

@SpringBootApplication
@EnableScheduling
public class MyjpaApplication implements CommandLineRunner {
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
	@ConditionalOnProperty(name = "seedDatabase", matchIfMissing = false)
	public InitializingBean seedDB(InventoryRepository ir) {
		return ()-> {
			ir.save(new Item("Label1", getExpirationDate(-1), "Type_1"));
			ir.save(new Item("Label2", getExpirationDate(10), "Type_2"));
			ir.save(new Item("Label3", getExpirationDate(4), "Type_3"));
		};
	}
	/**
	 * Display the seeded items, just for verification purpose
	 */
	@Bean
	@ConditionalOnProperty(name = "seedDatabase", matchIfMissing = false)
	public CommandLineRunner displayItems(InventoryRepository repository) {
		return args -> {
			repository.findAll().forEach(System.out::println); };
	}
   @Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                          .assignErrorJournal();
    }
    
    @Bean
    EventBus createEventBus(Environment env) {
	    return EventBus.create(env, Environment.THREAD_POOL);
    }

	@Autowired
	private EventBus eventBus;

	@Autowired
	private SimpleReceiver receiver;

	private static ApplicationContext app;
	
    @Override
	public void run(String... args) throws Exception {
    	eventBus.on($(Notificator.EXPIRED_EVENT), receiver);
    	eventBus.on($(Notificator.REMOVED_EVENT), receiver);
	}
    @PreDestroy
    public void cleanup() {
		app.getBean(Environment.class).shutdown();
    }
	public static void main(String[] args) {
		app = SpringApplication.run(MyjpaApplication.class, args); 
	}
}
