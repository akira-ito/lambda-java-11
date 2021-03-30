package com.zap.api.domain.property;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class BoundingBox {
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	public BoundingBox(double x1, double y1, double x2, double y2) {
		this.minX = Math.min(x1, x2);
		this.maxX = Math.max(x1, x2);
		this.minY = Math.min(y1, y2);
		this.maxY = Math.max(y1, y2);
	}

	public BoundingBox(Point point1, Point point2) {
		this(point1.x, point1.y, point2.x, point2.y);
	}

	public boolean contains(Point point) {
		return (point.x >= minX && point.x <= maxX && point.y >= minY && point.y <= maxY);
	}

	@Getter
	@AllArgsConstructor(staticName = "of")
	public static class Point {
		private double x;
		private double y;
	}
}