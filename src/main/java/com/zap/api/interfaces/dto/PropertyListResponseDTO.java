package com.zap.api.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.zap.api.domain.property.BusinessType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PropertyListResponseDTO {
	private String id;
	private List<String> images;
	private int bathrooms;
	private int bedrooms;
	private int usableAreas;
	private int parkingSpaces;
	private LocalDateTime createdAt;
	private PricingInfosResponseDTO pricingInfos;
	
	// TODO: DEBUG
	private double lat;
	private double lon;

	@Getter
	@Builder
	public static class PricingInfosResponseDTO {
		private BigDecimal yearlyIptu;
		private BigDecimal price;
		private BigDecimal rentalTotalPrice;
		private BusinessType businessType;
		private Integer monthlyCondoFee;
	}
}
