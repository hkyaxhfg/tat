package org.hkyaxhfg.tat.lang.util.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * εΊεεε¨.
 *
 * @author: wjf
 * @date: 2021/9/23 11:04
 */
@FunctionalInterface
public interface Serializer<T extends Serializable> extends Function<T, byte[]> {}
