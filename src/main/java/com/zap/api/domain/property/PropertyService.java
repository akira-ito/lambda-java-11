package com.zap.api.domain.property;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.zap.api.common.Page;
import com.zap.api.common.Pagination;
import com.zap.api.common.Pair;
import com.zap.api.domain.PortalOriginType;
import com.zap.api.domain.property.filter.PortalPropertyFilterService;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter.ContextPortalPropertyFilter;
import com.zap.api.interfaces.dto.PropertyListResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PropertyService {
	private final PropertyRepository propertyRepository;
	private final ConversionService conversionService;
	private final PortalPropertyFilterService portalPropertyFilterService;

	public Pagination<PropertyListResponseDTO> getAllByType(PortalOriginType type, Page page) {
		Predicate<Property> predicate = this.portalPropertyFilterService.getFilterByType(type,
				ContextPortalPropertyFilter.of(type));

		Pair<Long, Stream<Property>> findAll = this.propertyRepository.findAllByPredicate(predicate, page);
		List<PropertyListResponseDTO> propertiesDTO = findAll.getValue()
				.map(property -> this.conversionService.convert(property, PropertyListResponseDTO.class))
				.collect(Collectors.toList());
		return Pagination.of(page, findAll.getKey(), propertiesDTO);

//		List<PropertyListResponseDTO> dto = StreamSupport
//				.stream(paginationProperties.getListings().spliterator(), false)
//				.map(property -> this.conversionService.convert(property, PropertyListResponseDTO.class))
//				.collect(Collectors.toList());
//		return paginationProperties.fromDTO(dto);
	}
}
