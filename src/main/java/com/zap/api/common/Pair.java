package com.zap.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class Pair<K, V> {
	private K key;
	private V value;
}
