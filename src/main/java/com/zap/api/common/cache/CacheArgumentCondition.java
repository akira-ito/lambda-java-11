package com.zap.api.common.cache;

import java.time.Duration;

import lombok.Getter;

@Getter
public class CacheArgumentCondition<T> implements CacheCondition<T> {
	private final String name;
	private final Object[] args;
	private final CacheCondition<T> duration;

	public static <T> CacheArgumentCondition<T> of(String name, Duration duration, Object... args) {
		return new CacheArgumentCondition<>(name, duration, args);
	}

	public CacheArgumentCondition(String name, Duration duration, Object... args) {
		this.name = name;
		this.args = args;
		this.duration = new CacheDurationCondition<>(name, duration);
	}

	private CacheArgumentCondition(CacheArgumentCondition<T> cacheDuration, CacheCondition<T> condition) {
		this.name = cacheDuration.name;
		this.args = cacheDuration.args;
		this.duration = condition;
	}

	@Override
	public CacheCondition<T> saving() {
		return new CacheArgumentCondition<>(this, this.duration.saving());
	}

	@Override
	public boolean test(T t, CacheCondition<T> condition) {
		return this.duration.and((value, cond) -> {
			if (!(condition instanceof CacheArgumentCondition))
				return false;

			var argumentCondition = ((CacheArgumentCondition<T>) condition);
			if (this.args.length != argumentCondition.args.length)
				return false;

			for (int i = 0; i < args.length; i++) {
				if (!this.args[i].equals(argumentCondition.args[i]))
					return false;
			}
			return true;
		}).test(t, condition);
	}
}
