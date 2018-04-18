package org.solution.as.model;

import java.math.BigInteger;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;

/** 
 * This class represents a Product.
 * 
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

	@NotNull
	@Digits(integer = 10, fraction = 0)
	private BigInteger id;
	
	private String name;
	
	@Valid
	private Price current_price;
	
	public Product() {
		
	}
	
	/**
	 * This constructs a Product with id, name and Price
	 * @param id
	 * @param name
	 * @param current_price
	 */
	public Product(BigInteger id, String name, Price current_price) {
		this.id = id;
		this.name = name;
		this.current_price = current_price;
	}
	
	public BigInteger getId() {
		return id;
	}
	
	public void setId(BigInteger id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Price getCurrent_price() {
		return current_price;
	}
	
	public void setCurrent_price(Price current_price) {
		this.current_price = current_price;
	}
	
}
