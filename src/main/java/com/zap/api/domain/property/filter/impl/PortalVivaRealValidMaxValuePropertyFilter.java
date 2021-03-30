package com.zap.api.domain.property.filter.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.BoundingBoxService;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.IPortalPropertyRentalFilter;
import com.zap.api.domain.property.filter.IPortalPropertySaleFilter;
import com.zap.api.domain.property.filter.PortalPropertyFilterOrder;

@Component
public class PortalVivaRealValidMaxValuePropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertySaleFilter, IPortalPropertyRentalFilter {
//	private final MathContext mathContext = new MathContext(2);
	private final BoundingBoxService boundingBoxService;

	public PortalVivaRealValidMaxValuePropertyFilter(final BoundingBoxService boundingBoxService) {
		super(PortalPropertyFilterOrder.RENTAL_SALE_VALID_PRICE_MAX_VALUE_FILTER);
		this.boundingBoxService = boundingBoxService;
	}

	@Override
	public Predicate<Property> rentalFilter() {
		return property -> {
			var pricingInfos = property.getPricingInfos();
			var locationOpt = Optional
					.ofNullable(property.getAddress()).map(address -> address.getGeoLocation())
					.map(geoLocation -> geoLocation.getLocation());

			BigDecimal maxValue = BigDecimal.valueOf(4_000.0);
			if (locationOpt.isPresent()
					&& this.boundingBoxService.contains(locationOpt.get().getLon(), locationOpt.get().getLat())) {
				BigDecimal newLimit = maxValue.setScale(2).multiply(BigDecimal.valueOf(50))
						.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
				maxValue = maxValue.add(newLimit);
			}

			return maxValue.compareTo(pricingInfos.getRentalTotalPrice()) <= 0;
		};

	}

	@Override
	public Predicate<Property> saleFilter() {
		return property -> BigDecimal.valueOf(700_000.0).compareTo(property.getPricingInfos().getPrice()) <= 0;

	}
}
