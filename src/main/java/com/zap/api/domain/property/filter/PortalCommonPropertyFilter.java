package com.zap.api.domain.property.filter;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.GeoLocation;
import com.zap.api.domain.property.Location;
import com.zap.api.domain.property.Property;

@Component
public class PortalCommonPropertyFilter extends AbstractPortalPropertyFilter {

	public PortalCommonPropertyFilter() {
		super(PortalPropertyFilterOrder.COMMON_FILTER);
	}

	@Override
	Predicate<Property> rentalFilter() {
		return notLatLongEqualZero();
	}

	@Override
	Predicate<Property> saleFilter() {
		return notLatLongEqualZero();

	}

	private Predicate<Property> notLatLongEqualZero() {
		return property -> {
			GeoLocation geoLocation = property.getAddress().getGeoLocation();
			return !Location.of(0, 0).equals(geoLocation.getLocation());
		};
	}

}
