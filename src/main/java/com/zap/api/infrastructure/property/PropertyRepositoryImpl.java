package com.zap.api.infrastructure.property;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import com.zap.api.common.Page;
import com.zap.api.common.Pagination;
import com.zap.api.domain.PortalOriginType;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.PropertyRepository;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter;
import com.zap.api.domain.property.filter.PortalCommonPropertyFilter;
import com.zap.api.domain.property.filter.PortalVivaRealPropertyFilter;
import com.zap.api.domain.property.filter.PortalZapPropertyFilter;

@Repository
public class PropertyRepositoryImpl implements PropertyRepository {

	@Override
	public Pagination<Property> findAllByType(PortalOriginType type, Page page) {

		PortalCommonPropertyFilter commonFilter = new PortalCommonPropertyFilter();
		Predicate<Property> predicate = property -> true;
		predicate = predicate.and(commonFilter.rental().or(commonFilter.sale()));
		
		if (PortalOriginType.ZAP.equals(type)) {
			AbstractPortalPropertyFilter zapFilter = new PortalZapPropertyFilter();
			predicate = predicate.and(zapFilter.rental().or(zapFilter.sale()));
		} else if (PortalOriginType.VIVA_REAL.equals(type)) {
			AbstractPortalPropertyFilter zapFilter = new PortalVivaRealPropertyFilter();
			predicate = predicate.and(zapFilter.rental().or(zapFilter.sale()));
		}

		Stream<Property> stream = DataMock.properties.stream().filter(predicate)
				.sorted(Comparator.comparing(Property::getId));
		List<Property> properties = stream.collect(Collectors.toList());
//		Long totalCount = stream.count();
//		List<Property> all = stream.skip(page.getOffset()).limit(page.getPageSize()).collect(Collectors.toList());

		int skip = page.getOffset(), pageSize = page.getPageSize();
		List<Property> listings = new ArrayList<>();
		for (Property property : properties) {
			if (skip-- > 0)
				continue;
			if (pageSize-- <= 0)
				break;
			listings.add(property);
		}
		return Pagination.of(page.getPageNumber(), page.getPageSize(), properties.size(), listings);
	}

}
