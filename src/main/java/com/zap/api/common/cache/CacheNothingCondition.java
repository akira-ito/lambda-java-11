package com.zap.api.common.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CacheNothingCondition<T> implements CacheCondition<T> {
	private final String name;


	@Override
	public CacheCondition<T> saving() {
		return this;
	}

	@Override
	public boolean test(T t, CacheCondition<T> condition) {
		return false;
	}
}
