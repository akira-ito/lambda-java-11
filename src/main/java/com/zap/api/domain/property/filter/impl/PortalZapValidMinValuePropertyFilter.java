package com.zap.api.domain.property.filter.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class PortalZapValidMinValuePropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertySaleFilter, IPortalPropertyRentalFilter {
	private final BoundingBoxService boundingBoxService;
	private final ZapProperties properties;

	public PortalZapValidMinValuePropertyFilter(final BoundingBoxService boundingBoxService, ZapProperties properties) {
		super(PortalPropertyFilterOrder.RENTAL_SALE_VALID_PRICE_MIN_VALUE_FILTER);
		this.boundingBoxService = boundingBoxService;
		this.properties = properties;
	}

	@Override
	public BiPredicate<Property, ContextPortalPropertyFilter> rentalFilter() {
		return (property, context) -> {
			BigDecimal minRentalPrice = FilterPropertiesUtil
					.get(this.properties, context.getPortalOriginType(), FilterProperties::getMinRentalPrice)
					.setScale(2);

			return minRentalPrice
					/* BigDecimal.valueOf(3_500.00) */.compareTo(property.getPricingInfos().getRentalTotalPrice()) >= 0;
		};
	}

	@Override
	public BiPredicate<Property, ContextPortalPropertyFilter> saleFilter() {
		return (property, context) -> {
			var pricingInfos = property.getPricingInfos();
			var location = property.getAddress().getGeoLocation().getLocation();

			BigDecimal minSalePrice = FilterPropertiesUtil
					.get(this.properties, context.getPortalOriginType(), FilterProperties::getMinSalePrice).setScale(2);// BigDecimal.valueOf(600_000);

			if (this.boundingBoxService.contains(location.getLon(), location.getLat())) {
				BigDecimal boundingMinSalePricePercentage = BigDecimal.valueOf(FilterPropertiesUtil.get(this.properties,
						context.getPortalOriginType(), FilterProperties::getBoundingMinSalePricePercentage))
						.setScale(2);

				BigDecimal newValue = minSalePrice.setScale(2)
						.multiply(boundingMinSalePricePercentage /* BigDecimal.valueOf(10) */)
						.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
				minSalePrice = minSalePrice.add(newValue);
			}
			return minSalePrice.compareTo(pricingInfos.getPrice()) >= 0;
		};
	}
}
