package com.zap.api.common.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.zap.api.common.ZapApiError;

@ControllerAdvice
public class ControllerExceptionHandler /* extends ResponseEntityExceptionHandler */ {

	@ExceptionHandler(ConversionFailedException.class)
	public ResponseEntity<ZapApiError> handleConflict(RuntimeException ex) {
		return ResponseEntity.badRequest().body(ZapApiError.of(ex.getMessage(), ""));
	}

}
