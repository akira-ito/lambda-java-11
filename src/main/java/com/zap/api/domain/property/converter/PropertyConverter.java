package com.zap.api.domain.property.converter;

import org.springframework.core.convert.converter.Converter;

import com.zap.api.domain.property.PricingInfos;
import com.zap.api.domain.property.Property;
import com.zap.api.interfaces.dto.PropertyListResponseDTO;
import com.zap.api.interfaces.dto.PropertyListResponseDTO.PricingInfosResponseDTO.PricingInfosResponseDTOBuilder;
import com.zap.api.interfaces.dto.PropertyListResponseDTO.PropertyListResponseDTOBuilder;

public class PropertyConverter implements Converter<Property, PropertyListResponseDTO> {

	@Override
	public PropertyListResponseDTO convert(Property property) {

		PricingInfos pricingInfos = property.getPricingInfos();
		PricingInfosResponseDTOBuilder pricingInfosDTOBuilder = PropertyListResponseDTO.PricingInfosResponseDTO
				.builder().yearlyIptu(pricingInfos.getYearlyIptu()).price(pricingInfos.getPrice())
				.rentalTotalPrice(pricingInfos.getRentalTotalPrice()).businessType(pricingInfos.getBusinessType())
				.monthlyCondoFee(pricingInfos.getMonthlyCondoFee());
		PropertyListResponseDTOBuilder propertyListDTOBuilder = PropertyListResponseDTO.builder().id(property.getId())
				.images(property.getImages()).bathrooms(property.getBathrooms()).bedrooms(property.getBedrooms())
				.usableAreas(property.getUsableAreas()).createdAt(property.getCreatedAt())
				.pricingInfos(pricingInfosDTOBuilder.build());
		return propertyListDTOBuilder.build();

	}

}
