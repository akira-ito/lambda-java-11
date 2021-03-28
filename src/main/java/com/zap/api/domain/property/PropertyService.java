package com.zap.api.domain.property;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.zap.api.common.Page;
import com.zap.api.common.Pagination;
import com.zap.api.domain.PortalOriginType;
import com.zap.api.interfaces.dto.PropertyListResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PropertyService {
	private final PropertyRepository propertyRepository;
	private final ConversionService conversionService;


	public Pagination<PropertyListResponseDTO> getAllByType(PortalOriginType type, Page page) {
		Pagination<Property> paginationProperties = this.propertyRepository.findAllByType(type, page);
		List<PropertyListResponseDTO> dto = StreamSupport
				.stream(paginationProperties.getListings().spliterator(), false)
				.map(property -> this.conversionService.convert(property, PropertyListResponseDTO.class))
				.collect(Collectors.toList());
		return paginationProperties.fromDTO(dto);
	}
}
