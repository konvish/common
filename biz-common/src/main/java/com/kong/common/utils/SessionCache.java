package com.kong.common.utils;

/**
 * TODO 一句话描述该类用途
 * Created by kong on 2016/1/3.
 */
@Deprecated
public class SessionCache<K, V> extends LRUCache<K, V> {
    public SessionCache(int maxSize) {
        super(maxSize);
    }

    public SessionCache(int maxSize, int initialCapacity, float loadFactor, boolean accessOrder) {
        super(maxSize, initialCapacity, loadFactor, accessOrder);
    }
}
