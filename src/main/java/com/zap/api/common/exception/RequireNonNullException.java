package com.zap.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequireNonNullException extends RuntimeException {
	private static final long serialVersionUID = 5761482892893938042L;
	private final String message;
}
