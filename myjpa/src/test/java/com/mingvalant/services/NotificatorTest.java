package com.mingvalant.services;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.security.InvalidParameterException;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mingvalant.MyjpaApplication;
import com.mingvalant.model.Item;

import reactor.bus.EventBus;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyjpaApplication.class)
public class NotificatorTest {
	@Autowired
	EventBus eventBus;
	
	@Rule
	public OutputCapture capture = new OutputCapture();
	
	@Test
	public void testExpiredNotification() throws InterruptedException {
		Item item = new Item("labelExpired", new Date(), "typeExpired");
		Notificator target = new Notificator(eventBus);
		target.publishItemExpiration(item);
		assertThat(capture.toString(), containsString("<=Notificator sent an expiration EVENT for item "+item.getLabel()));
	}

	@Test
	public void testRemoveNotification() throws InterruptedException {
		Item item = new Item("labelRemove", new Date(), "typeRemove");
		Notificator target = new Notificator(eventBus);
		target.publishItemRemoved(item);
		assertThat(capture.toString(), containsString("<=Notificator sent a removed EVENT for item "+item.getLabel()));
	}

	@Test(expected=InvalidParameterException.class)
	public void testPublishItemRemovedWithNull()  throws InterruptedException {
		Notificator target = new Notificator(eventBus);
		target.publishItemRemoved(null);
		System.out.println("Should not get to here");
	}
}
