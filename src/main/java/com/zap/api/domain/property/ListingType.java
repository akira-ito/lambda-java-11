package com.zap.api.domain.property;

import java.util.stream.Stream;

public enum ListingType {
	USED;

	public static ListingType fromString(String listingType) {
		return Stream.of(values()).filter(type -> type.name().equalsIgnoreCase(listingType)).findFirst().orElse(null);
	}
}
