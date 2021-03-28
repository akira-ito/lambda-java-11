package com.zap.api.domain.property;

import java.util.stream.Stream;

public enum BusinessType {
	RENTAL, SALE;

	public static BusinessType fromString(String listingType) {
		return Stream.of(values()).filter(type -> type.name().equalsIgnoreCase(listingType)).findFirst().orElse(null);
	}
}
