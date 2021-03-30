package com.zap.api.domain.property;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.zap.api.config.ZapProperties;
import com.zap.api.domain.property.BoundingBox.Point;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoundingBoxService implements InitializingBean {
	private final ZapProperties properties;
	private BoundingBox boundingBox;

	@Override
	public void afterPropertiesSet() throws Exception {
		var boundingBoxProperties = this.properties.getBoundingBox();
		this.boundingBox = new BoundingBox(boundingBoxProperties.getMinLon(), boundingBoxProperties.getMinLat(),
				boundingBoxProperties.getMaxLon(), boundingBoxProperties.getMaxLon());
	}

	public boolean contains(double lon, double lat) {
		return this.boundingBox.contains(Point.of(lon, lat));
	}

}
