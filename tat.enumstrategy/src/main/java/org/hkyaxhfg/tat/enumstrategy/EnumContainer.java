package org.hkyaxhfg.tat.enumstrategy;

import java.util.List;
import java.util.Map;

/**
 * 枚举容器, 只有实现了{@link EnumStrategy}接口的枚举才会被收集到此容器中.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@FunctionalInterface
@SuppressWarnings("all")
public interface EnumContainer {

    /**
     * 查找所有的枚举.
     * @return Map<String, List<EnumStrategy<?>>>.
     */
    Map<String, List<EnumStrategy<?>>> findAll();

    /**
     * 通过全类名查找某个枚举.
     * @param className 全类名.
     * @return List<EnumStrategy<?>>.
     */
    default List<EnumStrategy<?>> findOne(String className) {
        return findAll().get(className);
    }

    /**
     * 通过全类名查找某个枚举.
     * @param clazz class.
     * @return List<EnumStrategy<?>>.
     */
    default List<EnumStrategy<?>> findOne(Class<? extends EnumStrategy<?>> clazz) {
        return findAll().get(clazz.getTypeName());
    }

}
