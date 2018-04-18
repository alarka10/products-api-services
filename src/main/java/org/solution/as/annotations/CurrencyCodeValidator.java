package org.solution.as.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.EnumUtils;

/**
 * This class validates the format of currency_code.
 *
 */

public class CurrencyCodeValidator implements ConstraintValidator<AcceptableCurrencyCode, String> {

	@Override
	public void initialize(AcceptableCurrencyCode acceptableCurrencyCode) {
		
	}

	@Override
	public boolean isValid(String currencyCode, ConstraintValidatorContext constraintContext) {
		return EnumUtils.isValidEnum(CurrencyCode.class, currencyCode);
	}

}
