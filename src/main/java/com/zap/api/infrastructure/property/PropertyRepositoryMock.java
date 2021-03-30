package com.zap.api.infrastructure.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import com.zap.api.common.Page;
import com.zap.api.common.Pagination;
import com.zap.api.common.Pair;
import com.zap.api.common.cache.CacheManager;
import com.zap.api.common.cache.CacheNothingCondition;
import com.zap.api.domain.PortalOriginType;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.PropertyRepository;
import com.zap.api.domain.property.filter.PortalPropertyFilterService;

@Repository
public class PropertyRepositoryMock implements PropertyRepository {
	private final PortalPropertyFilterService filterService;
//	private final DataMock dataMock;
	private final CacheManager<Collection<Property>> propertiesCache;

	public PropertyRepositoryMock(PortalPropertyFilterService filterService, DataMock dataMock) {
		this.filterService = filterService;
//		this.dataMock = dataMock;
		this.propertiesCache = new CacheManager<>(this, dataMock::loadAndGetProperties,
				List.of(CacheNothingCondition.of("findAllByPredicate")
				// new CacheDurationCondition<>("findAllByPredicate", Duration.ofSeconds(5))
				));
	}

	@Override
	public Pagination<Property> findAllByType(final PortalOriginType type, final Page page) {

		Predicate<Property> predicate = this.filterService.getFilterByType(type);

//		PortalCommonPropertyFilter commonFilter = new PortalCommonPropertyFilter();
//		Predicate<Property> predicate = property -> true;
//		predicate = predicate.and(commonFilter.rental().or(commonFilter.sale()));
//		
//		if (PortalOriginType.ZAP.equals(type)) {
//			AbstractPortalPropertyFilter zapFilter = new PortalZapPropertyFilter();
//			predicate = predicate.and(zapFilter.rental().or(zapFilter.sale()));
//		} else if (PortalOriginType.VIVA_REAL.equals(type)) {
//			AbstractPortalPropertyFilter zapFilter = new PortalVivaRealPropertyFilter();
//			predicate = predicate.and(zapFilter.rental().or(zapFilter.sale()));
//		}

		Stream<Property> stream = this.propertiesCache.getValueByCacheName("findAllByType").stream().filter(predicate)
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

	@Override
	public Pair<Long, Stream<Property>> findAllByPredicate(Predicate<Property> predicate, Page page) {
		Stream<Property> stream = this.propertiesCache.getValueByCacheName("findAllByPredicate").stream()
				.filter(predicate).sorted(Comparator.comparing(Property::getId));
		List<Property> properties = stream.collect(Collectors.toList());

		int skip = page.getOffset();
		int pageSize = skip + page.getPageSize();

		if (skip >= properties.size())
			return Pair.of(0L, Stream.empty());
		if (pageSize > properties.size())
			pageSize = properties.size();

		Stream<Property> propertyStream = properties.subList(skip, pageSize).stream();
		return Pair.of((long) properties.size(), propertyStream);
	}

}
