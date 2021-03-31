package com.zap.api.domain.property.filter.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiPredicate;

import org.springframework.stereotype.Component;

import com.zap.api.config.ZapProperties;
import com.zap.api.config.ZapProperties.FilterProperties;
import com.zap.api.config.ZapProperties.FilterProperties.FilterPropertiesUtil;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.IPortalPropertySaleFilter;
import com.zap.api.domain.property.filter.PortalPropertyFilterOrder;

@Component
public class PortalZapValidUsableAreasPropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertySaleFilter {
	private final ZapProperties properties;

	public PortalZapValidUsableAreasPropertyFilter(ZapProperties properties) {
		super(PortalPropertyFilterOrder.SALE_VALID_USABLE_AREA_FILTER);
		this.properties = properties;
	}

	@Override
	public BiPredicate<Property, ContextPortalPropertyFilter> saleFilter() {
		return (property, context) -> {
			if (property.getUsableAreas() <= 0)
				return false;

			BigDecimal minSquareMeterSalePrice = FilterPropertiesUtil
					.get(this.properties, context.getPortalOriginType(), FilterProperties::getMinSquareMeterSalePrice)
					.setScale(2);

			var pricingInfos = property.getPricingInfos();
			var pricePerMeter = pricingInfos.getPrice().divide(new BigDecimal(property.getUsableAreas()),
					RoundingMode.HALF_UP);

			return minSquareMeterSalePrice/* BigDecimal.valueOf(3_500) */.compareTo(pricePerMeter) < 0;
		};
	}
}
