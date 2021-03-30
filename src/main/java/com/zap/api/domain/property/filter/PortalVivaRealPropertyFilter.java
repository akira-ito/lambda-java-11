package com.zap.api.domain.property.filter;

import java.math.BigDecimal;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.BoundingBoxService;
import com.zap.api.domain.property.Property;

@Component
public class PortalVivaRealPropertyFilter extends AbstractPortalPropertyFilter {

	public PortalVivaRealPropertyFilter(final BoundingBoxService boundingBoxService) {
		super(PortalPropertyFilterOrder.VIVA_REAL_FILTER);
	}

	@Override
	Predicate<Property> rentalFilter() {
		return property -> BigDecimal.valueOf(4_000.0).compareTo(property.getPricingInfos().getRentalTotalPrice()) < 0;

	}

	@Override
	Predicate<Property> saleFilter() {
		return property -> BigDecimal.valueOf(700_000.0).compareTo(property.getPricingInfos().getPrice()) < 0;

	}
}
