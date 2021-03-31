package com.zap.api.common.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import com.zap.api.common.Pair;

public class CacheManager<T> {

	private final Object objectCached;
	private final Supplier<T> supplier;
	private Map<String, Pair<CacheCondition<T>, T>> cache = new HashMap<>();
//	private List<CacheCondition<T>> conditions;

	public CacheManager(Object objectCached, Supplier<T> supplier) {
		this(objectCached, supplier, List.of());
	}

	public CacheManager(Object objectCached, Supplier<T> supplier, List<CacheCondition<T>> conditions) {
		this.objectCached = objectCached;
		this.supplier = supplier;
//		this.conditions = conditions;
		conditions.forEach(condition -> {
			cache.put(condition.getName(), Pair.of(condition, null));
		});
	}

	public T getValueByCacheName(String name) {
		return this.getValueByCacheName(name, this.supplier, CacheNothingCondition.of(name));
	}

	@SuppressWarnings("unchecked")
	public <P extends T> P getValueByCacheName(String name, Supplier<P> supplier, CacheCondition<T> condition) {
		synchronized (this.objectCached) {
			this.cache.compute(name, (key, cacheValue) -> {
				if (Objects.isNull(cacheValue))
					cacheValue = Pair.of((CacheCondition<T>) condition, null);

				CacheCondition<T> cacheCondition = cacheValue.getKey();
				if (cacheCondition.test(cacheValue.getValue(), condition))
					return cacheValue;
				return Pair.of(cacheCondition.saving(condition), supplier.get());
			});

			return (P) this.cache.get(name).getValue();
		}
	}

	public void clear() {
		synchronized (this.objectCached) {
			this.cache = new HashMap<>();
		}
	}

	public static class CacheGenericManager extends CacheManager<Object> {

		public CacheGenericManager() {
			super(new Object(), null);
		}

		/**
		 * @implSpec Essa implementação sempre lançará uma
		 *           {@code UnsupportedOperationException}.
		 */
		@Override
		public Object getValueByCacheName(String name) {
			throw new UnsupportedOperationException();
		}
	}

}
