package com.example.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.example.MyjpaApplication;

import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class SimpleReceiver implements Consumer<Event<String>> {
	static Logger logger = Logger.getLogger(SimpleReceiver.class.getName());
	
	public void accept(Event<String> event) {
		Object okey =  event.getKey();
		if (okey instanceof String) {
			String key = (String) okey;
			String messsage = event.getData();
			logger.info(String.format("SimpleReceiver received event %s: %s", key, messsage));
		} else {
			logger.warn("Event consumer received an unknown event");
		}
	}
}
