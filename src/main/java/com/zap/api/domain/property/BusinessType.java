package com.zap.api.domain.property;

import java.util.stream.Stream;

public enum BusinessType {
	RENTAL, SALE;

	public static BusinessType fromString(String businessType) {
		return Stream.of(values()).filter(type -> type.name().equalsIgnoreCase(businessType)).findFirst().orElse(null);
	}
}
