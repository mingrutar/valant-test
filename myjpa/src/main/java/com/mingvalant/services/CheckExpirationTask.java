package com.mingvalant.services;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mingvalant.model.InventoryRepository;

@Component
public class CheckExpirationTask {
	private InventoryRepository inventoryRepository;
	private Notificator notificater;
	private static Logger logger = Logger.getLogger(CheckExpirationTask.class);

	@Autowired
	CheckExpirationTask(InventoryRepository inventoryRepository, Notificator notificater) {
		this.inventoryRepository = inventoryRepository;
		this.notificater = notificater;
	}
	/**
	 * TODO: scheduled interval is for demo. in real world, the value may need adjusted.
	 */
	@Scheduled(fixedRate = 60000)									// in ms,		
	public void checkExpirationItems() {
		inventoryRepository.findByExpirationBefore(new Date()).forEach( 
			(i) -> {try {
				notificater.publishItemExpiration(i);
			} catch (Exception e) {
				logger.warn("caught InterruptedException whick check expired items" );
			}} );
	}
}
