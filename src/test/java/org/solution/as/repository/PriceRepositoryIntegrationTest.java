package org.solution.as.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import org.junit.Test;
import org.solution.as.AbstractTest;
import org.solution.as.model.Price;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class tests that data is present in the database and the application has access to update the data.  
 * 
 * Data added to TABLE price at start of application
 * 
 * id       | currency_code | value
 * ---------+--------------+---------
 * 16696652 |          USD |    0.75
 * 15643793 |          USD |   99.99
 * 15117729 |          USD |   25.25
 * 16483589 |          USD | 5000.50
 * 16752456 |          USD |      99
 * 13860428 |          USD |   16.65
 */

public class PriceRepositoryIntegrationTest extends AbstractTest {

	@Autowired
	private PriceRepository priceRepository;
	
	/**
	 * This tests that an items are present in the database
	 */
	@Test
	public void test_findPrices() {
		Collection<Price> prices = priceRepository.findPrices();
		assertEquals("Expected count: ", 6, prices.size());
	}
	
	/**
	 * This tests that an item can be retrieved from the database
	 */
	@Test
	public void test_findPriceById_true() {
		Price price = priceRepository.findPriceById(id);
		assertNotNull("Price should not be empty", price);
		assertEquals("Expected currency_code: ", "USD", price.getCurrency_code());
		assertEquals("Expected value: ", value, price.getValue());
	}
	
	/**
	 * This tests the scenario when an item is not present in the database
	 */
	@Test
	public void test_findPriceById_false() {
		Price price = priceRepository.findPriceById(id1);
		assertNull("Price should be empty", price);
	}
	
	/**
	 * This tests that an item can be updated in the database if found
	 */
	@Test
	public void test_updatePriceById_True() {
		boolean updated = priceRepository.updatePriceById(id, value1);
		
		assertTrue("Price should be updated", updated);
		Price price = priceRepository.findPriceById(id);
		assertEquals("Expected currency_code: ", "USD", price.getCurrency_code());
		assertEquals("Expected value: ", value1, price.getValue());
		
		// Rollback
		priceRepository.updatePriceById(id, value);
	}
	
	/**
	 * This tests that an item is not updated/inserted in the database if not found
	 */
	@Test
	public void test_updatePriceById_False() {
		boolean updated = priceRepository.updatePriceById(id1, value1);
		
		assertFalse("Price should be updated", updated);
		Price price = priceRepository.findPriceById(id1);
		assertNull("Price should not be inserted", price);
	}
	
}
