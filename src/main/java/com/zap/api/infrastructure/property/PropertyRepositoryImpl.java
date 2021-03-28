package com.zap.api.infrastructure.property;

import java.math.BigDecimal;
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
import com.zap.api.domain.property.BusinessType;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.PropertyRepository;

@Repository
public class PropertyRepositoryImpl implements PropertyRepository {

	@Override
	public Pagination<Property> findAllByType(PortalOriginType type, Page page) {

		Predicate<Property> predicate = property -> true;
		if (PortalOriginType.ZAP.equals(type)) {
			predicate = property -> BusinessType.RENTAL.equals(property.getPricingInfos().getBusinessType())
					&& BigDecimal.valueOf(3_500.00).compareTo(property.getPricingInfos().getRentalTotalPrice()) > 0
			|| (BusinessType.SALE.equals(property.getPricingInfos().getBusinessType()) 
							&& BigDecimal.valueOf(600_000).compareTo(property.getPricingInfos().getPrice()) > 0 );
		} else if (PortalOriginType.VIVA_REAL.equals(type)) {
			predicate = property -> BusinessType.RENTAL.equals(property.getPricingInfos().getBusinessType())
					&& BigDecimal.valueOf(4_000.0).compareTo(property.getPricingInfos().getRentalTotalPrice()) < 0;
			predicate.or(property -> BusinessType.SALE.equals(property.getPricingInfos().getBusinessType())
					&& BigDecimal.valueOf(700_000.0).compareTo(property.getPricingInfos().getPrice()) < 0);
		}

		Stream<Property> stream = DataMock.properties.stream().filter(predicate).sorted(Comparator.comparing(Property::getId));
		List<Property> properties = stream.collect(Collectors.toList());
//		Long totalCount = stream.count();
//		List<Property> all = stream.skip(page.getOffset()).limit(page.getPageSize()).collect(Collectors.toList());
		
		int skip = page.getOffset(), pageSize = page.getPageSize();
		List<Property> listings = new ArrayList<>();
		for (Property property : properties) {
			if (skip-- > 0) continue;
			if (pageSize-- <= 0) break;
			listings.add(property);
		}
		return Pagination.of(page.getPageNumber(), page.getPageSize(), properties.size(), listings);
	}

}
