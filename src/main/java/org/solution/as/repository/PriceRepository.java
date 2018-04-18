package org.solution.as.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import org.solution.as.model.Price;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * This interface is used to retrieve/update data from database.
 *
 */

public interface PriceRepository extends CrudRepository<Price, String> {

	@Query("SELECT * FROM price WHERE id=?0")
	public Price findPriceById(BigInteger id);
	
	@Query("SELECT * FROM price")
	public Collection<Price> findPrices();
	
	@Query("UPDATE price SET value=?1 WHERE id=?0 IF EXISTS")
	public boolean updatePriceById(BigInteger id, BigDecimal value);
	
}
