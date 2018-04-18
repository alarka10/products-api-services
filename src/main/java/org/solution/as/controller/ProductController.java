package org.solution.as.controller;

import java.io.IOException;
import java.math.BigInteger;
import javax.validation.Valid;
import org.solution.as.model.Product;
import org.solution.as.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.support.exception.CassandraInvalidQueryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This is the main controller class. 
 *
 */

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	/**
	 * This method retrieves a Product
	 * @param id
	 * @return Product object
	 * @throws JsonProcessingException
	 * @throws IOException
	 * @throws MethodArgumentTypeMismatchException
	 */
	@RequestMapping(value="/api/v1/products/{id}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> getProduct(@PathVariable("id") BigInteger id)
			throws JsonProcessingException, IOException, MethodArgumentTypeMismatchException, CassandraInvalidQueryException {
		Product product = productService.findProductById(id);
		if (product == null) {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	/**
	 * This method updates the price of a product
	 * @param id
	 * @param product
	 * @return
	 * @throws HttpMessageNotReadableException
	 * @throws MethodArgumentNotValidException
	 */
	@RequestMapping(value="/api/v1/products/{id}",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> updateProductPrice(@PathVariable("id") BigInteger id, @Valid @RequestBody Product product)
			throws HttpMessageNotReadableException, MethodArgumentNotValidException, CassandraInvalidQueryException {
		if (!id.equals(product.getId())) {
			return new ResponseEntity<Product>(HttpStatus.NOT_MODIFIED);
		}
		boolean priceUpdated = productService.updateProductById(product);
		if (!priceUpdated) {
			return new ResponseEntity<Product>(HttpStatus.NOT_MODIFIED);
		}
		return new ResponseEntity<Product>(HttpStatus.OK);
	}
}
