package org.hkyaxhfg.tat.auth.user;

import org.hkyaxhfg.tat.auth.Auth;
import org.hkyaxhfg.tat.auth.AuthAnnotatedResolver;

/**
 * 注解 {@link UserAuth} 的注解解析器, {@link UserAuth}注解为用户鉴权注解.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@SuppressWarnings("all")
public class UserAuthAnnotatedResolver<UserInfo> extends AuthAnnotatedResolver<UserInfo, UserAuth> {

    public UserAuthAnnotatedResolver(Auth<UserInfo> auth) {
        super(auth, UserAuth.class);
    }

    @Override
    public UserInfo getInfo(UserAuth userAuth) {

        UserInfo userInfo = this.auth.get();

        if (!userAuth.nullable() && userInfo == null) {
            throw UserAuthException.newEx("用户未登录");
        }

        return userInfo;
    }
}
