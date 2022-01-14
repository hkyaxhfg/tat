package org.hkyaxhfg.tat.auth.oauth2;

import java.lang.annotation.*;

/**
 * OAuth2协议的鉴权注解.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface OAuth2 {
}
