package com.zap.api.domain.property.filter;

import static com.zap.api.domain.property.BusinessType.RENTAL;
import static com.zap.api.domain.property.BusinessType.SALE;

import java.util.function.Predicate;

import com.zap.api.domain.property.Property;

import lombok.Getter;

@Getter
public abstract class AbstractPortalPropertyFilter {
	private PortalPropertyFilterOrder filterOrder;

	public AbstractPortalPropertyFilter(PortalPropertyFilterOrder filterOrder) {
		this.filterOrder = filterOrder;
	}

	public Predicate<Property> predicate() {
		return this.rental().or(this.sale());
	}

	public final Predicate<Property> rental() {
		Predicate<Property> filter = property -> RENTAL.equals(property.getPricingInfos().getBusinessType());
		return filter.and(this.rentalFilter());
	};

	public final Predicate<Property> sale() {
		Predicate<Property> filter = property -> SALE.equals(property.getPricingInfos().getBusinessType());
		return filter.and(this.saleFilter());
	};

	Predicate<Property> rentalFilter() {
		return property -> true;
	};

	Predicate<Property> saleFilter() {
		return property -> true;
	}
}
