package com.zap.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ZapApiError {
	private String message;
	private String code;
}
