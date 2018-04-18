package org.solution.as;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.runner.RunWith;
import org.solution.Application;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This is the parent class for Unit and Integration tests.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		classes=Application.class)
public abstract class AbstractTest {
	
	protected static BigInteger id = new BigInteger("16696652");
	protected static BigDecimal value = new BigDecimal("0.75");
	protected static BigInteger id1 = new BigInteger("123456789");
	protected static BigDecimal value1 = new BigDecimal("100.25");
	
}
