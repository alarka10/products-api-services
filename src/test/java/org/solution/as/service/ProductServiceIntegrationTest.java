package org.solution.as.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;
import org.solution.as.AbstractTest;
import org.solution.as.model.Price;
import org.solution.as.model.Product;
import org.solution.as.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.support.exception.CassandraInvalidQueryException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This class tests the service class.
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
 *
 */

public class ProductServiceIntegrationTest extends AbstractTest {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private PriceRepository priceRepository;
	
	/**
	 * This tests the scenario when a Product is found 
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@Test
	public void test_findProductById() throws JsonProcessingException, IOException, CassandraInvalidQueryException {
		Product product = service.findProductById(id);
		assertEquals("Expected currency code: ", "USD", product.getCurrent_price().getCurrency_code());
		assertEquals("Expected value: ", value, product.getCurrent_price().getValue());
	}
	
	/**
	 * This tests the scenario when a Product is not found
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@Test
	public void test_findProductById_null() throws JsonProcessingException, IOException, CassandraInvalidQueryException {
		Product product = service.findProductById(id1);
		assertNull("Product should be null ", product);
	}
	
	/**
	 * This tests the scenario when a Product is found and is updated
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@Test
	public void test_updateProductById_true() throws JsonProcessingException, IOException, CassandraInvalidQueryException {
		Price newPrice = new Price(id, value1, "USD");
		Product newProduct = new Product(id, "Update unit test", newPrice);
		boolean updated = service.updateProductById(newProduct);
		assertTrue("Price should be updated ", updated);
		
		// Assert updated price from repository
		Price updatedPrice = priceRepository.findPriceById(id);
		assertEquals("Expected currency_code: ", "USD", updatedPrice.getCurrency_code());
		assertEquals("Expected value: ", value1, updatedPrice.getValue());
		
		// Rollback original values for id: 16696652
		priceRepository.updatePriceById(id, value);
	}
	
	/**
	 * This tests the scenario when a Product is not found and not updated
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@Test
	public void test_updateProductById_false() throws JsonProcessingException, IOException, CassandraInvalidQueryException {
		Price newPrice = new Price(id1, value, "USD");
		Product newProduct = new Product(id1, "Update unit test", newPrice);
		boolean updated = service.updateProductById(newProduct);
		assertFalse("Price should NOT be updated ", updated);
	}
}
