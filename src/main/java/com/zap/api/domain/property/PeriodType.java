package com.zap.api.domain.property;

import java.util.stream.Stream;

public enum PeriodType {
	MONTHLY;

	public static PeriodType fromString(String periodType) {
		return Stream.of(values()).filter(type -> type.name().equalsIgnoreCase(periodType)).findFirst().orElse(null);
	}
}
