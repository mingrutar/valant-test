package com.mingvalant.services;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
			eventBus.notify(EXPIRED_EVENT, Event.wrap(item.toString()));
			long delta = Calendar.getInstance().getTime().getTime() - item.getExpiration().getTime();
			logger.info(String.format("<=Notificator sent an expiration EVENT for item %s that has expired %d day(s).", 
					item.getLabel(), TimeUnit.MILLISECONDS.toDays(delta)));
		} else {
			throw new  InvalidParameterException();
		}
	}
	public void publishItemRemoved(Item item) throws InterruptedException {
		if (item != null) {
			eventBus.notify(REMOVED_EVENT, Event.wrap(item.toString()));
			logger.info(String.format("<=Notificator sent a removed EVENT for item %s", item.getLabel()));
		} else {
			throw new  InvalidParameterException();
		}
	}
}
