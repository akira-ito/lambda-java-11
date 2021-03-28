package com.zap.api.domain.property;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class Address {
	private String city;
	private String neighborhood;
	private GeoLocation geoLocation;
}
