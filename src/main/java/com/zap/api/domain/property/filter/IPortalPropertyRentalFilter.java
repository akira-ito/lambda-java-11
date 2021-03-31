package com.zap.api.domain.property.filter;

import static com.zap.api.domain.property.BusinessType.RENTAL;

import java.util.function.BiPredicate;

import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter.ContextPortalPropertyFilter;

public interface IPortalPropertyRentalFilter {

	default BiPredicate<Property, ContextPortalPropertyFilter> rental() {
		BiPredicate<Property, ContextPortalPropertyFilter> filter = (property, context) -> RENTAL
				.equals(property.getPricingInfos().getBusinessType());
		return filter.and(this.rentalFilter());
	};

	public BiPredicate<Property, ContextPortalPropertyFilter> rentalFilter();
}
