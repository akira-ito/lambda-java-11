package com.zap.api.domain;

import lombok.Getter;

public enum PortalOriginType {
	VIVA_REAL("VIVA_REAL"), ZAP("ZAP");

	private @Getter String name;

	private PortalOriginType(String name) {
		this.name = name;
	}

}
