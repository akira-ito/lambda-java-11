package com.zap.api.domain.property.filter;

import java.math.BigDecimal;
import java.util.function.Predicate;

import com.zap.api.domain.property.Property;

public class PortalVivaRealPropertyFilter extends AbstractPortalPropertyFilter {

	@Override
	Predicate<Property> rentalFilter() {
		return property -> BigDecimal.valueOf(4_000.0).compareTo(property.getPricingInfos().getRentalTotalPrice()) < 0;

	}

	@Override
	Predicate<Property> saleFilter() {
		return property -> BigDecimal.valueOf(700_000.0).compareTo(property.getPricingInfos().getPrice()) < 0;

	}
}
