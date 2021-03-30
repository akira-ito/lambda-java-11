package com.zap.api.domain.property.filter;

import java.util.List;

import com.zap.api.domain.PortalOriginType;

import lombok.Getter;

@Getter
public enum PortalPropertyFilterOrder {
	COMMON_FILTER(PortalOriginType.ZAP, PortalOriginType.VIVA_REAL),
	ZAP_VALID_USABLE_AREA_FILTER(PortalOriginType.ZAP), ZAP_FILTER(PortalOriginType.ZAP),
	VIVA_REAL_FILTER(PortalOriginType.VIVA_REAL);

	private List<PortalOriginType> types;

	PortalPropertyFilterOrder(PortalOriginType... types) {
		this.types = List.of(types);
	}
}
