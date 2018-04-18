package org.solution.as.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.solution.as.annotations.AcceptableCurrencyCode;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This class represents a Price.
 *
 */

@Table
@JsonIgnoreProperties(ignoreUnknown = true, value = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Price {
	
	@PrimaryKey
	@Digits(integer = 10, fraction = 0)
	private BigInteger id;
	
	@NotNull
	@Digits(integer = 100, fraction = 2)
	private BigDecimal value;
	
	@NotNull
	@AcceptableCurrencyCode
	private String currency_code;
	
	public Price() {
		
	}
	
	/**
	 * This constructs a Price with id, value and currency_code
	 * @param id
	 * @param value
	 * @param currency_code
	 */
	public Price(BigInteger id, BigDecimal value, String currency_code) {
		this.id = id;
		this.value = value;
		this.currency_code = currency_code;
	}
	
	public BigInteger getId() {
		return id;
	}
	
	public void setId(BigInteger id) {
		this.id = id;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	
}
