package com.mingvalant.services;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mingvalant.model.Item;

import reactor.bus.Event;
import reactor.bus.EventBus;

@Service
public class Notificator {
	static public final String REMOVED_EVENT = "removed";
	static public final String EXPIRED_EVENT = "expired";

	static Logger logger = Logger.getLogger(Notificator.class.getName());
	
	EventBus eventBus;
	@Autowired
	public Notificator(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void publishItemExpiration(Item item) throws InterruptedException {
		if (item != null) {
			long start = System.currentTimeMillis();
			eventBus.notify(EXPIRED_EVENT, Event.wrap(item.toString()));
	
			long elapsed = System.currentTimeMillis() - start;
			logger.info("publish Item Expiration elapsed time: " + elapsed + "ms");
		} else {
			throw new  InvalidParameterException();
		}
	}
	public void publishItemRemoved(Item item) throws InterruptedException {
		if (item != null) {
			long start = System.currentTimeMillis();
			eventBus.notify(REMOVED_EVENT, Event.wrap(item.toString()));
	
			long elapsed = System.currentTimeMillis() - start;
			logger.info("publish Item Removed elapsed time: " + elapsed + "ms");
		} else {
			throw new  InvalidParameterException();
		}
	}
}
