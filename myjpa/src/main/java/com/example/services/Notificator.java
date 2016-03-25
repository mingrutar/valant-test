package com.example.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Item;

import reactor.bus.Event;
import reactor.bus.EventBus;

@Service
public class Notificator {
	static public final String REMOVED_EVENT = "removed";
	static public final String EXPIRED_EVENT = "expired";

	static Logger logger = Logger.getLogger(Notificator.class.getName());
	
	@Autowired
	EventBus eventBus;

	public void publishItemExpiration(Item item) throws InterruptedException {
		long start = System.currentTimeMillis();
		eventBus.notify(EXPIRED_EVENT, Event.wrap(item.toString()));

		long elapsed = System.currentTimeMillis() - start;
		logger.info("publish Item Expiration elapsed time: " + elapsed + "ms");
	}
	public void publishItemRemoved(Item item) throws InterruptedException {
		long start = System.currentTimeMillis();
		eventBus.notify(REMOVED_EVENT, Event.wrap(item.toString()));

		long elapsed = System.currentTimeMillis() - start;
		logger.info("publish Item Removed elapsed time: " + elapsed + "ms");
	}
}
