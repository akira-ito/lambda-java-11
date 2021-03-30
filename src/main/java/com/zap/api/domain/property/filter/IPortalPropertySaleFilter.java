package com.zap.api.domain.property.filter;

import static com.zap.api.domain.property.BusinessType.SALE;

import java.util.function.Predicate;

import com.zap.api.domain.property.Property;

public interface IPortalPropertySaleFilter {

	default Predicate<Property> sale() {
		Predicate<Property> filter = property -> SALE.equals(property.getPricingInfos().getBusinessType());
		return filter.and(this.saleFilter());
	};


	Predicate<Property> saleFilter();
}
