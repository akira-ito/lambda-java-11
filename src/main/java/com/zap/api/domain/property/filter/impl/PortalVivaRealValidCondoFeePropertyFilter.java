package com.zap.api.domain.property.filter.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.springframework.stereotype.Component;

import com.zap.api.config.ZapProperties;
import com.zap.api.config.ZapProperties.FilterProperties;
import com.zap.api.config.ZapProperties.FilterProperties.FilterPropertiesUtil;
import com.zap.api.domain.property.BoundingBoxService;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.IPortalPropertyRentalFilter;
import com.zap.api.domain.property.filter.PortalPropertyFilterOrder;

@Component
public class PortalVivaRealValidCondoFeePropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertyRentalFilter {
	private final ZapProperties properties;

	public PortalVivaRealValidCondoFeePropertyFilter(final BoundingBoxService boundingBoxService,
			ZapProperties properties) {
		super(PortalPropertyFilterOrder.RENTAL_VALID_CONDO_FEE_FILTER);
		this.properties = properties;
	}

	@Override
	public BiPredicate<Property, ContextPortalPropertyFilter> rentalFilter() {
		return (property, context) -> {
			var pricingInfos = property.getPricingInfos();
			if (Objects.isNull(pricingInfos.getMonthlyCondoFee()))
				return false;

			double maxCondoFeePercentage = FilterPropertiesUtil
					.get(this.properties, context.getPortalOriginType(), FilterProperties::getMaxCondoFeePercentage);

			
			BigDecimal limitCondoFee = pricingInfos.getRentalTotalPrice().setScale(2).multiply(BigDecimal
					.valueOf(maxCondoFeePercentage/* 30 */))
					.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
			return limitCondoFee.compareTo(pricingInfos.getMonthlyCondoFee()) > 0;
		};

	}
}