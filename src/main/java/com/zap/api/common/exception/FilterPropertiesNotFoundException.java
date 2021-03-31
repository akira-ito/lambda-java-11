package com.zap.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterPropertiesNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 6906053923070744469L;
	private String message;

}
