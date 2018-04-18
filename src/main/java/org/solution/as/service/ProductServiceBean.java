package org.solution.as.service;

import java.io.IOException;
import java.math.BigInteger;
import org.solution.as.model.Price;
import org.solution.as.model.Product;
import org.solution.as.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is the main service class.
 *
 */

@Service
@PropertySource(value={"classpath:/config/application.properties"})
public class ProductServiceBean implements ProductService {

	@Autowired
	private PriceRepository priceRepository;
	
	@Value("${external.api.url}")
	private String url;
	
	@Value("${external.api.url.excludes.fields}")
	private String excludeFields;
	
	private RestTemplate restTemplate;
	private ObjectMapper mapper;
	private StringBuilder uri;
	
	public ProductServiceBean() {
		restTemplate = new RestTemplate();
		mapper = new ObjectMapper();
		uri = new StringBuilder();
	}

	/**
	 * This method calls an external API to retrieve the name of a product.
	 * @param id
	 * @return name of a product
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private String getProductName(BigInteger id) throws JsonProcessingException, IOException {
		ResponseEntity<String> response = null;
		String title = null;
		try {
			response = restTemplate.getForEntity(uri.append(this.url).append(id).append(this.excludeFields).toString(), String.class);
		} catch (HttpClientErrorException ex) {
			//Do nothing
		}
		if (response != null) {
			JsonNode root = null, product = null, item = null, description = null;
			root = mapper.readTree(response.getBody());
			
			// Traverse the JSON response to extract the title (product name)
			if (root != null) {
				product = root.path("product");
				if (product != null) {
					item = product.path("item");
					if (item != null) {
						description = item.path("product_description");
						if (description != null) {
							title = description.path("title").textValue();
						}
					}
				}
			}
		}
		return title;
	} 
	
	/**
	 * This method aggregates product data from multiple (price database and external API call) sources
	 * @param price
	 * @return Product object
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private Product getProduct(Price price) throws JsonProcessingException, IOException {
		String productName = getProductName(price.getId());
		return new Product(price.getId(), productName, price);
	}
	
	@Override
	public Product findProductById(BigInteger id) throws JsonProcessingException, IOException {
		Price price = priceRepository.findPriceById(id);
		if (price == null) {
			return null;
		}
		return getProduct(price);
	}

	@Override
	public boolean updateProductById(Product product) {
		return priceRepository.updatePriceById(product.getId(), product.getCurrent_price().getValue());
	}
}
