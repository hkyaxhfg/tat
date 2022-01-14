package org.hkyaxhfg.tat.lang.util.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 反序列化器.
 *
 * @author: wjf
 * @date: 2021/9/23 11:06
 */
@FunctionalInterface
public interface Deserializer<T extends Serializable> extends Function<byte[], T> {}
