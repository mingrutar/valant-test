package com.mingvalant.services;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.Date;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mingvalant.MyjpaApplication;
import com.mingvalant.model.Item;
import com.mingvalant.services.Notificator;

import reactor.bus.Event;
import reactor.bus.EventBus;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyjpaApplication.class)
public class NotificatorTest {
	EventBus mockEventBus;
	Item item;
	
    @Before
    public void setup() throws Exception {
    	mockEventBus = mock(EventBus.class);
    	item = new Item("label1", new Date(), "type1");
    }
// TODO: Cannot make Mockito to verify generic type.
//    
//	@Test
//	public void testPublishItemExpiration() throws InterruptedException {
//		Notificator target = new Notificator(mockEventBus);
//		target.publishItemExpiration(item);
//		
//		verify(mockEventBus).notify(Notificator.EXPIRED_EVENT, any(Even.class));
//	}
//
//	@Test
//	public void testPublishItemRemoved()  throws InterruptedException {
//		Notificator target = new Notificator(mockEventBus);
//		target.publishItemRemoved(item);
//		verify(mockEventBus).notify(Notificator.REMOVED_EVENT, any(Even.class));
//	}
	@Test(expected=InvalidParameterException.class)
	public void testPublishItemRemovedWithNull()  throws InterruptedException {
		Notificator target = new Notificator(mockEventBus);
		target.publishItemRemoved(null);
		verify(mockEventBus).notify(Notificator.REMOVED_EVENT, any(Event.class));
	}
}
