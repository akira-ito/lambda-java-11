package com.zap.api.domain.property.filter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.zap.api.domain.property.BoundingBoxService;
import com.zap.api.domain.property.Property;

@Component
public class PortalZapPropertyFilter extends AbstractPortalPropertyFilter {
	private final BoundingBoxService boundingBoxService;
	private final MathContext mathContext = new MathContext(2);

	public PortalZapPropertyFilter(final BoundingBoxService boundingBoxService) {
		super(PortalPropertyFilterOrder.ZAP_FILTER);
		this.boundingBoxService = boundingBoxService;
	}

	@Override
	Predicate<Property> rentalFilter() {
		return property -> BigDecimal.valueOf(3_500.00).compareTo(property.getPricingInfos().getRentalTotalPrice()) > 0;
	}

	@Override
	Predicate<Property> saleFilter() {
		return property -> {
//			if (property.getUsableAreas() <= 0)
//				return false;
//
			var pricingInfos = property.getPricingInfos();
//			var pricePerMeter = pricingInfos.getPrice().divide(new BigDecimal(property.getUsableAreas()),
//					this.mathContext);
//			if (BigDecimal.valueOf(3_500).compareTo(pricePerMeter) >= 0)
//				return false;

			var location = property.getAddress().getGeoLocation().getLocation();
			BigDecimal minValue = BigDecimal.valueOf(600_000);
			if (this.boundingBoxService.contains(location.getLon(), location.getLat())) {
				BigDecimal discount = minValue.multiply(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(100),
						this.mathContext);
				minValue = minValue.subtract(discount);
			}
			return minValue.compareTo(pricingInfos.getPrice()) > 0;
		};
	}
}
