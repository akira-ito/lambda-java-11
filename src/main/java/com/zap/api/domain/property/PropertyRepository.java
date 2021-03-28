package com.zap.api.domain.property;

import com.zap.api.common.Page;
import com.zap.api.common.Pagination;
import com.zap.api.domain.PortalOriginType;

/**
 * Interface para representar o repositorio do imóvel.
 * 
 * @author Edson Akira Ito
 * @since v1
 *
 */
public interface PropertyRepository {

	Pagination<Property> findAllByType(PortalOriginType type, Page page);

}
