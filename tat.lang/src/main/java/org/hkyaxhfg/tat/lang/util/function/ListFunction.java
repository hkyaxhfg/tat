package org.hkyaxhfg.tat.lang.util.function;

import java.util.List;
import java.util.function.Function;

/**
 * ListFunction, 仅仅是为了方便List数据结构使用.
 *
 * @author: wjf
 * @date: 2022/1/10
 */
@FunctionalInterface
public interface ListFunction<T, R> extends Function<List<T>, List<R>> {}
