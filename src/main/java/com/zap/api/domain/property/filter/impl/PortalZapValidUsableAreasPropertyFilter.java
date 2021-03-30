package com.zap.api.domain.property.filter.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.IPortalPropertySaleFilter;
import com.zap.api.domain.property.filter.PortalPropertyFilterOrder;

@Component
public class PortalZapValidUsableAreasPropertyFilter extends AbstractPortalPropertyFilter
		implements IPortalPropertySaleFilter {

	public PortalZapValidUsableAreasPropertyFilter() {
		super(PortalPropertyFilterOrder.SALE_VALID_USABLE_AREA_FILTER);
	}

	@Override
	public Predicate<Property> saleFilter() {
		return property -> {
			if (property.getUsableAreas() <= 0)
				return false;

			var pricingInfos = property.getPricingInfos();
			var pricePerMeter = pricingInfos.getPrice().divide(new BigDecimal(property.getUsableAreas()),
					RoundingMode.HALF_UP);

			return BigDecimal.valueOf(3_500).compareTo(pricePerMeter) < 0;
		};
	}
}
