package org.solution.as.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.solution.as.AbstractControllerTest;
import org.solution.as.model.Product;
import org.solution.as.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * This class tests the controllers. It connects to the database.
 *
 */
public class ProductControllerIntegrationTest extends AbstractControllerTest {
	
	@Autowired
	private ProductService productService;
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	/**
	 * This tests that the GET method returns status 200 (OK) when a Product is found
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_getProduct_ok() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 200, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("id ", 16696652, jsonObject.get("id"));
		assertEquals("Price value ", value, ((Map<String, Object>)jsonObject.get("current_price")).get("value"));
		assertEquals("Price currency_code ", "USD", ((Map<String, Object>)jsonObject.get("current_price")).get("currency_code"));
	}
	
	/**
	 * This tests that the GET method returns status 404 (Not Found) when a Product is not found
	 * @throws Exception
	 */
	@Test
	public void test_getProduct_notFound() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id1).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 404, status);
	}
	
	/**
	 * This tests that the GET method returns status 400 (Bad Request) when a Product id in URL is not number
	 * @throws Exception
	 */
	@Test
	public void test_getProduct_idNotNumber() throws Exception {
		String id = "abcd";
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	/**
	 * This tests that the PUT method returns status 200 (Not Found) when a Product is updated
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_ok() throws Exception {
		Product product = productService.findProductById(id);
		Product updatedProduct = getProductObject(product.getId(), product.getName(), value1, "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 200, status);
		
		//Rollback
		mvc.perform(MockMvcRequestBuilders.put(uri, id)
										.contentType(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.content(super.mapToJson(product)))
									.andReturn();
	}
	
	/**
	 * This tests that the PUT method returns status 200 (Not Found) when a Product is updated but request body "name" field did not different from what is retrieved using GET request
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_withNotMatchingName_ok() throws Exception {
		Product product = productService.findProductById(id);
		Product updatedProduct = getProductObject(product.getId(), "New product name", value1, "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 200, status);
		
		//Rollback
		mvc.perform(MockMvcRequestBuilders.put(uri, id)
										.contentType(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.content(super.mapToJson(product)))
									.andReturn();
	}
	
	/**
	 * This tests that the PUT method returns status 200 (Not Found) when a Product is updated but request body did not have "name" field
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_withoutName_ok() throws Exception {
		Product product = productService.findProductById(id);
		Product updatedProduct = getProductObject(product.getId(), null, value1, "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 200, status);
		
		//Rollback
		mvc.perform(MockMvcRequestBuilders.put(uri, id)
										.contentType(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.content(super.mapToJson(product)))
									.andReturn();
	}
	
	/**
	 * This tests that the PUT method returns status 304 (Not Modified) when a Product id does not match between URL and request body
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_idNotMatching_NotModified() throws Exception {
		Product product = productService.findProductById(id);
		Product updatedProduct = getProductObject(id1, product.getName(), value1, "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 304, status);
	}
	
	/**
	 * This tests that the PUT method returns status 304 (Not Modified) when a Product id not found in database
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_idNotFound_NotModified() throws Exception {
		Product updatedProduct = getProductObject(id1, null, value1, "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id1)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 304, status);
	}

}
