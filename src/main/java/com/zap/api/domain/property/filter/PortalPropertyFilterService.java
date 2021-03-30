package com.zap.api.domain.property.filter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zap.api.common.exception.RequireNonNullException;
import com.zap.api.domain.PortalOriginType;
import com.zap.api.domain.property.Property;

@Service
public class PortalPropertyFilterService {
	private final Map<PortalOriginType, Predicate<Property>> portalPropertyFilters = new HashMap<>();

	@Autowired
	public PortalPropertyFilterService(List<AbstractPortalPropertyFilter> portalPropertyFilters) {
		Comparator<AbstractPortalPropertyFilter> comparing = Comparator.comparing(
				AbstractPortalPropertyFilter::getFilterOrder,
				(filterA, filterB) -> filterA.ordinal() - filterB.ordinal());

		portalPropertyFilters.stream().sorted(comparing).forEachOrdered(filter -> {
			filter.getFilterOrder().getTypes().forEach(type -> {
				this.portalPropertyFilters.compute(type, (key, oldValue) -> Optional.ofNullable(oldValue)
						.orElse(property -> true).and(filter.predicate()));
			});
		});
	}

	public Predicate<Property> getFilterByType(PortalOriginType type) {
		var portalPropertyFilter = this.portalPropertyFilters.get(type);
//		Objects.requireNonNull(portalPropertyFilter, "portalPropertyFilter must not be null.");
		if (Objects.isNull(portalPropertyFilter))
			throw new RequireNonNullException("The PortalPropertyFilter not found.");

		return portalPropertyFilter;
	}

}
