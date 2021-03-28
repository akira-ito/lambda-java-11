package com.zap.api.domain.property;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class GeoLocation {
	private PrecisionType precision;
	private Location location;
}
