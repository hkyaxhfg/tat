package org.hkyaxhfg.tat.lang.reflect;

import java.lang.annotation.Annotation;

/**
 * 反射器接口.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public interface Reflector {

    /**
     * 当前反射元素上是否有此注解.
     * @param clazz 注解.
     * @param <A> 注解.
     * @return boolean.
     */
    <A extends Annotation> boolean hasAnnotation(Class<A> clazz);

    /**
     * 获取反射元素上的注解.
     * @param clazz 注解.
     * @param <A> 注解.
     * @return 注解.
     */
    <A extends Annotation> A getAnnotation(Class<A> clazz);

    /**
     * 获取反射元素的名称.
     * @return 反射元素的名称.
     */
    String toReflectName();

}
