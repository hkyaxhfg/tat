package org.hkyaxhfg.tat.validation.group;

/**
 * 读操作.
 *
 * @author: wjf
 * @date: 2022/1/19
 */
public interface ReadGroup {

    /**
     * 查询单个.
     */
    interface FindOne {}

    /**
     * 查询列表.
     */
    interface FindList {}

    /**
     * 查询分页.
     */
    interface FindPage {}

}
