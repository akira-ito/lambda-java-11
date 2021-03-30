package com.zap.api.domain.property.filter;

import static com.zap.api.domain.property.BusinessType.RENTAL;

import java.util.function.Predicate;

import com.zap.api.domain.property.Property;

public interface IPortalPropertyRentalFilter {

	default Predicate<Property> rental() {
		Predicate<Property> filter = property -> RENTAL.equals(property.getPricingInfos().getBusinessType());
		return filter.and(this.rentalFilter());
	};

	public Predicate<Property> rentalFilter();
}
