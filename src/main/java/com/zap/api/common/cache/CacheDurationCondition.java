package com.zap.api.common.cache;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

import lombok.Getter;

@Getter
public class CacheDurationCondition<T> implements CacheCondition<T> {
	private final String name;
	private final Duration duration;
	private LocalTime time;

	public CacheDurationCondition(String name, Duration duration) {
		this.name = name;
		this.duration = duration;
	}

	private CacheDurationCondition(CacheDurationCondition<T> cacheDuration, LocalTime time) {
		this(cacheDuration.name, cacheDuration.duration);
		this.time = time;
	}

	@Override
	public CacheCondition<T> saving(CacheCondition<T> condition) {
		return new CacheDurationCondition<>(this, LocalTime.now());
	}

	@Override
	public boolean test(T t, CacheCondition<T> condition) {
		return !Objects.isNull(this.time) && Duration.between(this.time, LocalTime.now()).compareTo(this.duration) < 0;
	}
}
