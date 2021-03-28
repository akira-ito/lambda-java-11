package com.zap.api.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Page {
	private final int pageNumber;
	private final int pageSize;

	public static Page of(int pageNumber, int pageSize) {
		return new Page(Math.max(pageNumber, 1), pageSize);
	}

	public int getOffset() {
		return Math.max(this.pageNumber - 1, 0) * this.pageSize;
	}
}
