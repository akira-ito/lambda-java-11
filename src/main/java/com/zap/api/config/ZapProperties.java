package com.zap.api.config;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

import com.zap.api.common.exception.FilterPropertiesNotFoundException;
import com.zap.api.domain.PortalOriginType;

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
	private Map<PortalOriginType, FilterProperties> filter;

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

	@Getter
	@Setter
	public static class FilterProperties {
		private BigDecimal minSalePrice;
		private BigDecimal minRentalPrice;
		private double boundingMinSalePricePercentage;
		private BigDecimal minSquareMeterSalePrice;
		///
		private BigDecimal maxSalePrice;
		private BigDecimal maxRentalPrice;
		private double boundingMaxRentalPricePercentage;
		private double maxCondoFeePercentage;

		public static class FilterPropertiesUtil {
			public static <R> R get(ZapProperties zapProperties, PortalOriginType portalOriginType,
					Function<FilterProperties, R> mapper) {
				return Optional.ofNullable(zapProperties).map(properties -> properties.getFilter())
						.map(properties -> properties.get(portalOriginType)).map(mapper)
						.orElseThrow(() -> new FilterPropertiesNotFoundException(
								"FilterProperties not found for " + portalOriginType));

			}
		}
	}
}
