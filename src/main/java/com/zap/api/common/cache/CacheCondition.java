package com.zap.api.common.cache;

import java.util.function.BiPredicate;

public interface CacheCondition<T> extends BiPredicate<T, CacheCondition<T>> {
	String getName();

	CacheCondition<T> saving(CacheCondition<T> condition);
}
