package org.solution.as.exception;

import org.solution.as.model.Product;
import org.springframework.cassandra.support.exception.CassandraInvalidQueryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * This class handles the exceptions thrown by the controller class.
 *
 */

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(
			{MethodArgumentNotValidException.class,
			HttpMessageNotReadableException.class,
			MethodArgumentTypeMismatchException.class,
			CassandraInvalidQueryException.class})
	public ResponseEntity<Product> handleErrors(Exception ex) {
		return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
	}
	
}

