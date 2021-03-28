package com.zap.api.domain.property;

import com.zap.api.common.Page;
import com.zap.api.common.Pagination;
import com.zap.api.domain.PortalOriginType;

public interface PropertyRepository {

	Pagination<Property> findAllByType(PortalOriginType type, Page page);

}
