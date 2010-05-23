package com.navy.docman.util;

import java.io.Serializable;

public class PairData<K, V> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -9177834843359985343L;

	private K key = null;

	private V value = null;

	public PairData() {
		// noop
	}

	public PairData(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
