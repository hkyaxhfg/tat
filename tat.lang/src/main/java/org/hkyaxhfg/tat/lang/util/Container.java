package org.hkyaxhfg.tat.lang.util;

/**
 * 一个存放元素的容器, 主要用来逃避jvm的final检查, 在lambda表达式中经常需要使用.
 * @param <T> 元素.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class Container<T> {

    /**
     * 元素.
     */
    private T element;

    /**
     * 放入元素.
     * @param element 元素.
     */
    public void put(T element) {
        this.element = element;
    }

    /**
     * 拿出元素.
     * @return 元素.
     */
    public T get() {
        return this.element;
    }

    @Override
    public String toString() {
        return "Container{" +
                "element=" + element +
                '}';
    }

}
