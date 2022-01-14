package org.hkyaxhfg.tat.auth.user;

import java.lang.annotation.*;

/**
 * 用户鉴权注解.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UserAuth {

    /**
     * 用户信息是否可以为null, 默认为false.
     * @return false.
     */
    boolean nullable() default false;

}
