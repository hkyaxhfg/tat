package org.hkyaxhfg.tat.enumstrategy;

import java.lang.annotation.*;

/**
 * 枚举序列化, 用来标记枚举的哪些字段需要被序列化, 反序列化默认采取Enum.name的方式.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumSerialization {
}
