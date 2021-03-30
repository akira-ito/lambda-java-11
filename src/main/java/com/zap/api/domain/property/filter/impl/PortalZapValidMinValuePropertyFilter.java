package com.zap.api.domain.property.filter.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.BoundingBoxService;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.IPortalPropertyRentalFilter;
import com.zap.api.domain.property.filter.IPortalPropertySaleFilter;
import com.zap.api.domain.property.filter.PortalPropertyFilterOrder;

@Component
public class PortalZapValidMinValuePropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertySaleFilter, IPortalPropertyRentalFilter {
	private final BoundingBoxService boundingBoxService;
//	private final MathContext mathContext = new MathContext(2);

	public PortalZapValidMinValuePropertyFilter(final BoundingBoxService boundingBoxService) {
		super(PortalPropertyFilterOrder.RENTAL_SALE_VALID_PRICE_MIN_VALUE_FILTER);
		this.boundingBoxService = boundingBoxService;
	}

	@Override
	public Predicate<Property> rentalFilter() {
		return property -> BigDecimal.valueOf(3_500.00)
				.compareTo(property.getPricingInfos().getRentalTotalPrice()) >= 0;
	}

	@Override
	public Predicate<Property> saleFilter() {
		return property -> {
			var pricingInfos = property.getPricingInfos();
			var location = property.getAddress().getGeoLocation().getLocation();

			BigDecimal minValue = BigDecimal.valueOf(600_000);
			if (this.boundingBoxService.contains(location.getLon(), location.getLat())) {
				BigDecimal discount = minValue.setScale(2).multiply(BigDecimal.valueOf(10))
						.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
				minValue = minValue.subtract(discount);
			}
			return minValue.compareTo(pricingInfos.getPrice()) >= 0;
		};
	}
}
