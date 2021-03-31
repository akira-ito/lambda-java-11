package com.zap.api.domain.property.filter;

import static com.zap.api.domain.property.BusinessType.SALE;

import java.util.function.BiPredicate;

import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter.ContextPortalPropertyFilter;

public interface IPortalPropertySaleFilter {

	default BiPredicate<Property, ContextPortalPropertyFilter> sale() {
		BiPredicate<Property, ContextPortalPropertyFilter> filter = (property, context) -> SALE
				.equals(property.getPricingInfos().getBusinessType());
		return filter.and(this.saleFilter());
	};

	BiPredicate<Property, ContextPortalPropertyFilter> saleFilter();
}
