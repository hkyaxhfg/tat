package org.hkyaxhfg.tat.enumstrategy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.Unaware;

/**
 * 枚举策略序列化.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@JsonSerialize(using = JacksonEnumStrategySerializer.class)
@JsonDeserialize(using = JacksonEnumStrategyDeserializer.class)
public interface EnumStrategy<E extends Enum<E>> {

    /**
     * 获取枚举的name.
     * @return name.
     */
    default String name() {
        return strongCheck().name();
    }

    /**
     * 获取枚举定义的顺序号.
     * @return ordinal.
     */
    default int ordinal() {
        return strongCheck().ordinal();
    }

    /**
     * 获取枚举的声明class.
     * @return Class<? extends Enum<?>>.
     */
    default Class<E> declaringClass() {
        return Unaware.castUnaware(strongCheck().getDeclaringClass());
    }

    /**
     * 枚举强检验.
     * @return Enum<E>.
     */
    default Enum<E> strongCheck() {
        if (!(this instanceof Enum<?>)) {
            TatException.throwEx("{}不是一个Enum<?>", this.getClass().getTypeName());
        }
        return Unaware.castUnaware(this);
    }

}
