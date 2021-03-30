package com.zap.api.domain.property.filter.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.BoundingBoxService;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.IPortalPropertyRentalFilter;
import com.zap.api.domain.property.filter.PortalPropertyFilterOrder;

@Component
public class PortalVivaRealValidCondoFeePropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertyRentalFilter {
//	private final MathContext mathContext = new MathContext(3);

	public PortalVivaRealValidCondoFeePropertyFilter(final BoundingBoxService boundingBoxService) {
		super(PortalPropertyFilterOrder.RENTAL_VALID_CONDO_FEE_FILTER);
	}

	@Override
	public Predicate<Property> rentalFilter() {
		return property -> {
			var pricingInfos = property.getPricingInfos();
			if (Objects.isNull(pricingInfos.getMonthlyCondoFee()))
				return false;

			BigDecimal limitCondoFee = pricingInfos.getRentalTotalPrice().setScale(2).multiply(BigDecimal.valueOf(30))
					.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
			return limitCondoFee.compareTo(pricingInfos.getMonthlyCondoFee()) > 0;
		};

	}
}