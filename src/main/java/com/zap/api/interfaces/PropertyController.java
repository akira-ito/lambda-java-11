package com.zap.api.interfaces;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zap.api.common.Page;
import com.zap.api.common.Pagination;
import com.zap.api.common.cache.CacheArgumentCondition;
import com.zap.api.common.cache.CacheDurationCondition;
import com.zap.api.common.cache.CacheManager;
import com.zap.api.common.cache.CacheNothingCondition;
import com.zap.api.common.cache.CacheManager.CacheGenericManager;
import com.zap.api.config.ZapProperties;
import com.zap.api.domain.PortalOriginType;
import com.zap.api.domain.property.PropertyService;
import com.zap.api.interfaces.dto.PropertyListResponseDTO;

import lombok.RequiredArgsConstructor;

/**
 * Classe que representa os endpoints para os imóveis.
 * 
 * @author Edson Akira Ito
 * @version v1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/properties")
public class PropertyController implements InitializingBean {
	private final PropertyService propertyService;
	private final ZapProperties zapProperties;
	private CacheGenericManager serviceCache;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.serviceCache = new CacheGenericManager();
	}

	/**
	 * @param portalOriginType A origem do {@link PortalOriginType portal}
	 * @param pageNumber       O número da pagina.
	 * @param pageSize         A quantidade de imóveis por pagina.
	 * @return A lista de imóveis paginada.
	 */
	@GetMapping()
	public ResponseEntity<Pagination<PropertyListResponseDTO>> list(
			@RequestParam(value = "portalOriginType") PortalOriginType portalOriginType,
			@RequestParam(value = "pageNumber", required = false) Optional<Integer> pageNumber,
			@RequestParam(value = "pageSize", required = false) Optional<Integer> pageSize) {

		Page page = Page.of(pageNumber.orElseGet(this.zapProperties.getPagination()::getPageNumber),
				pageSize.orElseGet(this.zapProperties.getPagination()::getPageSize));
		Supplier<Pagination<PropertyListResponseDTO>> supplier = () -> this.propertyService
				.getAllByType(portalOriginType, page);
		var properties = this.serviceCache.getValueByCacheName("getAllByType", supplier,
//				CacheNothingCondition.of("getAllByType")
				CacheArgumentCondition.of("getAllByType", Duration.ofSeconds(8), portalOriginType, page)
		);

//		properties = this.propertyService.getAllByType(portalOriginType,
//				Page.of(pageNumber.orElseGet(this.zapProperties.getPagination()::getPageNumber),
//						pageSize.orElseGet(this.zapProperties.getPagination()::getPageSize)));
		return ResponseEntity.ok(properties);
	}
}
