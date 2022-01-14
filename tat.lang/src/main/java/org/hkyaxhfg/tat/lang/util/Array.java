package org.hkyaxhfg.tat.lang.util;

import java.util.Iterator;

/**
 * 数组的通用迭代器, {@link E}为数组元素.
 *
 * @author: wjf
 * @date: 2021/9/26 10:52
 */
public class Array<E> implements Iterator<E> {

    /**
     * 任何类型的数组，包括基本数据类型数组
     */
    private final Object array;

    /**
     * 当前元素的index
     */
    private int currentIndex = 0;

    /**
     * 私有的构造方法，需要传入一个数组。
     *
     * @param array {@link E}
     */
    public Array(Object array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return this.currentIndex < java.lang.reflect.Array.getLength(this.array);
    }

    @Override
    public E next() {
        return Unaware.castUnaware(java.lang.reflect.Array.get(this.array, this.currentIndex++));
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("不能删除数组元素");
    }

}
