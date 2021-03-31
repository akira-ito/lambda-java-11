package com.zap.api.domain.property.filter.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.springframework.stereotype.Component;

import com.zap.api.config.ZapProperties;
import com.zap.api.config.ZapProperties.FilterProperties;
import com.zap.api.config.ZapProperties.FilterProperties.FilterPropertiesUtil;
import com.zap.api.domain.property.BoundingBoxService;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.IPortalPropertyRentalFilter;
import com.zap.api.domain.property.filter.IPortalPropertySaleFilter;
import com.zap.api.domain.property.filter.PortalPropertyFilterOrder;

@Component
public class PortalVivaRealValidMaxValuePropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertySaleFilter, IPortalPropertyRentalFilter {
	private final BoundingBoxService boundingBoxService;
	private final ZapProperties properties;

	public PortalVivaRealValidMaxValuePropertyFilter(final BoundingBoxService boundingBoxService,
			ZapProperties properties) {
		super(PortalPropertyFilterOrder.RENTAL_SALE_VALID_PRICE_MAX_VALUE_FILTER);
		this.boundingBoxService = boundingBoxService;
		this.properties = properties;
	}

	@Override
	public BiPredicate<Property, ContextPortalPropertyFilter> rentalFilter() {
		return (property, context) -> {
			var pricingInfos = property.getPricingInfos();
			var locationOpt = Optional.ofNullable(property.getAddress()).map(address -> address.getGeoLocation())
					.map(geoLocation -> geoLocation.getLocation());

			BigDecimal maxRentalPrice = FilterPropertiesUtil
					.get(this.properties, context.getPortalOriginType(), FilterProperties::getMaxRentalPrice)
					.setScale(2); // BigDecimal.valueOf(4_000.0);
			if (locationOpt.isPresent()
					&& this.boundingBoxService.contains(locationOpt.get().getLon(), locationOpt.get().getLat())) {
				BigDecimal boundingMaxRentalPricePercentage = BigDecimal
						.valueOf(FilterPropertiesUtil.get(this.properties, context.getPortalOriginType(),
								FilterProperties::getBoundingMaxRentalPricePercentage))
						.setScale(2);

				BigDecimal newLimit = maxRentalPrice.setScale(2)
						.multiply(boundingMaxRentalPricePercentage/* BigDecimal.valueOf(50) */)
						.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
				maxRentalPrice = maxRentalPrice.add(newLimit);
			}

			return maxRentalPrice.compareTo(pricingInfos.getRentalTotalPrice()) <= 0;
		};

	}

	@Override
	public BiPredicate<Property, ContextPortalPropertyFilter> saleFilter() {
		return (property, context) -> {
			BigDecimal maxSalePrice = FilterPropertiesUtil
					.get(this.properties, context.getPortalOriginType(), FilterProperties::getMaxSalePrice).setScale(2);

			return maxSalePrice
					/* BigDecimal.valueOf(700_000.0) */.compareTo(property.getPricingInfos().getPrice()) <= 0;
		};

	}
}
