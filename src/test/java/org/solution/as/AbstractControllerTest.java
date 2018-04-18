package org.solution.as;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.solution.as.controller.ProductController;
import org.solution.as.model.Price;
import org.solution.as.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.json.JsonSlurper;

/**
 * This is the parent class for the Integration tests.
 *
 */

@WebAppConfiguration
public abstract class AbstractControllerTest extends AbstractTest {

	protected MockMvc mvc;
	protected JsonSlurper jsonSlurper;
	protected String uri = "/api/v1/products/{id}";
	
	@Autowired
	protected WebApplicationContext webApplicationContext;
	
	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		jsonSlurper = new JsonSlurper();
	}
	
	protected void setUp(ProductController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
		jsonSlurper = new JsonSlurper();
	}
	
	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
	
	protected Product getProductObject(BigInteger id, String name, BigDecimal priceValue, String currencyCode) {
		Price price = new Price(id, priceValue, currencyCode); 
		Product product = new Product(id, name, price);
		return product;
	}
}
