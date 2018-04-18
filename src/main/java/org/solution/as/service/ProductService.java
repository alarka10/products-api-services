package org.solution.as.service;

import java.io.IOException;
import java.math.BigInteger;
import org.solution.as.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProductService {
	
	/**
	 * Retrieves a Product object
	 * @param id
	 * @return Product object
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	Product findProductById(BigInteger id) throws JsonProcessingException, IOException;
	
	/**
	 * Updated the price of a product
	 * @param newProduct
	 * @return true if price is updated, else false
	 */
	boolean updateProductById(Product newProduct);	
}
