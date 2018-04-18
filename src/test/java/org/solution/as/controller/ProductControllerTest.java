package org.solution.as.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.solution.as.AbstractControllerTest;
import org.solution.as.model.Product;
import org.solution.as.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * This class tests the controllers using Mocks. 
 * It does not connect to the database, instead uses mocks.
 *
 */

public class ProductControllerTest extends AbstractControllerTest {
	
	@Mock
	private ProductService productService;
	
	@InjectMocks
	private ProductController productController;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		super.setUp(productController);
	}
	
	/**
	 * This tests that the GET method returns status 200 (OK) when a Product is found
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_getProduct_ok() throws Exception {
		Product product = getProductObject(id, "Test product name", value, "USD");
		
		when(productService.findProductById(id)).thenReturn(product);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		verify(productService, times(1)).findProductById(id);
		
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
		
		when(productService.findProductById(id1)).thenReturn(null);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id1).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		verify(productService, times(1)).findProductById(id1);
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 404, status);
	}
	
	/**
	 * This tests that the PUT method returns status 200 (Not Found) when a Product is updated
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_ok() throws Exception {
		Product product = getProductObject(id, "Test product name", value, "USD");
		
		when(productService.updateProductById(any(Product.class))).thenReturn(true);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		verify(productService, times(1)).updateProductById(any(Product.class));
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 200, status);
	}
	
	/**
	 * This tests that the PUT method returns status 200 (Not Found) when a Product is updated but request body did not have "name" field
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_withoutName_ok() throws Exception {
		Product product = getProductObject(id, null, value, "USD");
		
		when(productService.updateProductById(any(Product.class))).thenReturn(true);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		verify(productService, times(1)).updateProductById(any(Product.class));
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 200, status);
	}
	
	/**
	 * This tests that the PUT method returns status 304 (Not Modified) when a Product id does not match between URL and request body
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_idNotMatching_NotModified() throws Exception {
		Product product = getProductObject(id1, "Test product name", value, "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
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
		Product product = getProductObject(id1, null, value, "USD");
		
		when(productService.updateProductById(any(Product.class))).thenReturn(false);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id1)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		verify(productService, times(1)).updateProductById(any(Product.class));
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 304, status);
	}
	
	/**
	 * This tests that the PUT method returns status 400 (Bad Request) when a Price value is null
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_withNullPriceValue_BadRequest() throws Exception {
		Product product = getProductObject(id, "Test product name", null, "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	/**
	 * This tests that the PUT method returns status 400 (Bad Request) when a Price value has invalid format
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_withInvalidPriceValue_BadRequest() throws Exception {
		Product product = getProductObject(id, "Test product name", new BigDecimal("100.7559"), "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	/**
	 * This tests that the PUT method returns status 400 (Bad Request) when a Price currency_code is not in UPPERCASE
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_withPriceCurrencyCode_caseSensitive_BadRequest() throws Exception {
		Product product = getProductObject(id, "Test product name", value, "usd");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	/**
	 * This tests that the PUT method returns status 400 (Bad Request) when a Price currency_code is invalid
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_withInvalidPriceCurrencyCode_BadRequest() throws Exception {
		Product product = getProductObject(id, "Test product name", value, "invalid");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	/**
	 * This tests that the PUT method returns status 400 (Bad Request) when a Price currency_code is null
	 * @throws Exception
	 */
	@Test
	public void test_updateProductPrice_withNullPriceCurrencyCode_BadRequest() throws Exception {
		Product product = getProductObject(id, "Test product name", value, null);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
}
