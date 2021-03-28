package com.zap.api.domain.property.filter;

import java.math.BigDecimal;
import java.util.function.Predicate;

import com.zap.api.domain.property.Property;

public class PortalZapPropertyFilter extends AbstractPortalPropertyFilter {

	@Override
	Predicate<Property> rentalFilter() {
		return property -> BigDecimal.valueOf(3_500.00).compareTo(property.getPricingInfos().getRentalTotalPrice()) > 0;

	}

	@Override
	Predicate<Property> saleFilter() {
		return property -> BigDecimal.valueOf(600_000).compareTo(property.getPricingInfos().getPrice()) > 0;
	}
}
