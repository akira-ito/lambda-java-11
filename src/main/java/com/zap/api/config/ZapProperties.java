package com.zap.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "zap")
public class ZapProperties {
	private PaginationProperties pagination;
	private BoundingBoxProperties boundingBox;

	@Getter
	@Setter
	public static class PaginationProperties {
		private Integer pageNumber;
		private Integer pageSize;
	}

	@Getter
	@Setter
	public static class BoundingBoxProperties {
		private double minLon;
		private double minLat;
		private double maxLon;
		private double maxLat;
	}
}
