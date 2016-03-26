package com.mingvalant.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mingvalant.MyjpaApplication;
import com.mingvalant.model.InventoryRepository;
import com.mingvalant.model.Item;
import com.mingvalant.services.CheckExpirationTask;
import com.mingvalant.services.Notificator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyjpaApplication.class)
public class CheckExpirationTaskTest {
	private InventoryRepository mockInventoryRepository = mock(InventoryRepository.class);
	private Notificator mockNotificater = mock(Notificator.class);
	List<Item> expired = new ArrayList<Item>();

	@Test
	public void testCheckExpirationItems() throws InterruptedException {
		CheckExpirationTask target = 
				new CheckExpirationTask(mockInventoryRepository, mockNotificater);
		for (int i = 0; i < 4; i++){
			expired.add(new Item(String.format("label%d", i), new Date(), String.format("type%d", i)));
		}
		when(mockInventoryRepository.findByExpirationBefore(any(Date.class))).thenReturn(expired);
		target.checkExpirationItems();
		verify(mockInventoryRepository).findByExpirationBefore(any(Date.class));
		verify(mockNotificater).publishItemExpiration(expired.get(0));
		verify(mockNotificater).publishItemExpiration(expired.get(1));
		verify(mockNotificater).publishItemExpiration(expired.get(2));
		verify(mockNotificater).publishItemExpiration(expired.get(3));
	}

}
