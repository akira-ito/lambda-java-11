package com.zap.api.domain.property.filter;

import java.util.List;

import com.zap.api.domain.PortalOriginType;

import lombok.Getter;

@Getter
public enum PortalPropertyFilterOrder {
	COMMON_FILTER(PortalOriginType.ZAP, PortalOriginType.VIVA_REAL),
	SALE_VALID_USABLE_AREA_FILTER(PortalOriginType.ZAP),
	RENTAL_SALE_VALID_PRICE_MIN_VALUE_FILTER(PortalOriginType.ZAP),
	RENTAL_VALID_CONDO_FEE_FILTER(PortalOriginType.VIVA_REAL),
	RENTAL_SALE_VALID_PRICE_MAX_VALUE_FILTER(PortalOriginType.VIVA_REAL);

	private List<PortalOriginType> types;

	PortalPropertyFilterOrder(PortalOriginType... types) {
		this.types = List.of(types);
	}
}
