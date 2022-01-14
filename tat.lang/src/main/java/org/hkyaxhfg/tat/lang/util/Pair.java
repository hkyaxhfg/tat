package org.hkyaxhfg.tat.lang.util;

/**
 * 键值对, 用来存放k-v形式的数据, 数据一旦创建不可改变.
 *
 * @author: wjf
 * @date: 2022/1/8
 */
public class Pair<K, V> {

    /**
     * key.
     */
    private K key;

    /**
     * value.
     */
    private V value;

    /**
     * 构造方法.
     */
    public Pair() {}

    /**
     * 构造方法, 需要一个key和value.
     * @param key key.
     * @param value value.
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
