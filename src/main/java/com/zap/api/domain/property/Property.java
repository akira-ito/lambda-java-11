package com.zap.api.domain.property;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class Property {
	private int usableAreas;
	private ListingType listingType;
	private LocalDateTime createdAt;
	private ListingStatusType listingStatus;
	private String id;
	private Integer parkingSpaces;
	private LocalDateTime updatedAt;
	private boolean owner;
	private List<String> images;
	private Address address;
	private int bathrooms;
	private int bedrooms;
	private PricingInfos pricingInfos;
}
