package com.zap.api.domain.property.filter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.BoundingBoxService;
import com.zap.api.domain.property.Property;

@Component
public class PortalZapValidUsableAreasPropertyFilter extends AbstractPortalPropertyFilter {
	private final MathContext mathContext = new MathContext(2);

	public PortalZapValidUsableAreasPropertyFilter(final BoundingBoxService boundingBoxService) {
		super(PortalPropertyFilterOrder.ZAP_VALID_USABLE_AREA_FILTER);
	}

	@Override
	Predicate<Property> saleFilter() {
		return property -> {
			if (property.getUsableAreas() <= 0)
				return false;

			var pricingInfos = property.getPricingInfos();
			var pricePerMeter = pricingInfos.getPrice().divide(new BigDecimal(property.getUsableAreas()),
					this.mathContext);
			if (BigDecimal.valueOf(3_500).compareTo(pricePerMeter) >= 0)
				return false;

			return true;
		};
	}
}
