package com.zap.api.domain.property;

import java.util.stream.Stream;

public enum PrecisionType {
	ROOFTOP, APPROXIMATE, RANGE_INTERPOLATED, GEOMETRIC_CENTER, NO_GEOCODE;

	public static PrecisionType fromString(String listingType) {
		return Stream.of(values()).filter(type -> type.name().equalsIgnoreCase(listingType)).findFirst().orElse(null);
	}
}
