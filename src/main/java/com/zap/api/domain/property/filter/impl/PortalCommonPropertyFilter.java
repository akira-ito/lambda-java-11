package com.zap.api.domain.property.filter.impl;

import java.util.function.BiPredicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.GeoLocation;
import com.zap.api.domain.property.Location;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.IPortalPropertyRentalFilter;
import com.zap.api.domain.property.filter.IPortalPropertySaleFilter;
import com.zap.api.domain.property.filter.PortalPropertyFilterOrder;

@Component
public class PortalCommonPropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertyRentalFilter, IPortalPropertySaleFilter {

	public PortalCommonPropertyFilter() {
		super(PortalPropertyFilterOrder.COMMON_FILTER);
	}

	@Override
	public BiPredicate<Property, ContextPortalPropertyFilter> rentalFilter() {
		return notLatLongEqualZero();
	}

	@Override
	public BiPredicate<Property, ContextPortalPropertyFilter> saleFilter() {
		return notLatLongEqualZero();

	}

	private BiPredicate<Property, ContextPortalPropertyFilter> notLatLongEqualZero() {
		return (property, context) -> {
			GeoLocation geoLocation = property.getAddress().getGeoLocation();
			return !Location.of(0, 0).equals(geoLocation.getLocation());
		};
	}

}
