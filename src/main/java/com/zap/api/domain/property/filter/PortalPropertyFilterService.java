package com.zap.api.domain.property.filter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zap.api.common.exception.RequireNonNullException;
import com.zap.api.domain.PortalOriginType;
import com.zap.api.domain.property.BusinessType;
import com.zap.api.domain.property.Property;
import com.zap.api.domain.property.filter.AbstractPortalPropertyFilter.ContextPortalPropertyFilter;

@Service
public class PortalPropertyFilterService {
	private final Map<PortalOriginType, Map<BusinessType, BiPredicate<Property, ContextPortalPropertyFilter>>> portalPropertyFilters = new HashMap<>();

	@Autowired
	public PortalPropertyFilterService(List<AbstractPortalPropertyFilter> portalPropertyFilters) {
		Comparator<AbstractPortalPropertyFilter> comparing = Comparator.comparing(
				AbstractPortalPropertyFilter::getFilterOrder,
				(filterA, filterB) -> filterA.ordinal() - filterB.ordinal());

		portalPropertyFilters.stream().sorted(comparing).forEachOrdered(filter -> {
			filter.getFilterOrder().getTypes().forEach(type -> {
				this.portalPropertyFilters.compute(type, (key, oldValue) -> {
					Map<BusinessType, BiPredicate<Property, ContextPortalPropertyFilter>> map = Optional
							.ofNullable(oldValue).orElse(new HashMap<>());

					if (filter instanceof IPortalPropertySaleFilter) {
						map.compute(BusinessType.SALE, (businessKey, filterValue) -> Optional.ofNullable(filterValue)
								.orElse((property, context) -> true).and(((IPortalPropertySaleFilter) filter).sale()));
					}
					if (filter instanceof IPortalPropertyRentalFilter) {
						map.compute(BusinessType.RENTAL,
								(businessKey, filterValue) -> Optional.ofNullable(filterValue)
										.orElse((property, context) -> true)
										.and(((IPortalPropertyRentalFilter) filter).rental()));
					}
					return map;
				});
			});
		});

	}

	public Predicate<Property> getFilterByType(PortalOriginType type, ContextPortalPropertyFilter context) {
		var portalPropertyFilter = this.portalPropertyFilters.get(type);
//		Objects.requireNonNull(portalPropertyFilter, "portalPropertyFilter must not be null.");
		if (Objects.isNull(portalPropertyFilter))
			throw new RequireNonNullException("The PortalPropertyFilter not found.");

		BiPredicate<Property, ContextPortalPropertyFilter> predicate = portalPropertyFilter.values().stream()
				.reduce((property, ctx) -> false, BiPredicate::or);
		return property -> predicate.test(property, context);
	}

}
