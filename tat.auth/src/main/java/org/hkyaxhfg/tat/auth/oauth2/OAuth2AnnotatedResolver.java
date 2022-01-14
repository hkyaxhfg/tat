package org.hkyaxhfg.tat.auth.oauth2;

import org.hkyaxhfg.tat.auth.Auth;
import org.hkyaxhfg.tat.auth.AuthAnnotatedResolver;

/**
 * 注解 {@link OAuth2} 的注解解析器, {@link OAuth2}注解为OAuth2协议鉴权注解.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
public class OAuth2AnnotatedResolver<OAuth2Info extends OAuth2Metadata> extends AuthAnnotatedResolver<OAuth2Info, OAuth2> {

    public OAuth2AnnotatedResolver(Auth<OAuth2Info> auth) {
        super(auth, OAuth2.class);
    }

    @Override
    public OAuth2Info getInfo(OAuth2 oAuth2) {
        OAuth2Info oAuth2Info = auth.get();

        if (oAuth2Info == null) {
            throw OAuth2Exception.newEx("不具有OAuth2访问权限");
        }
        return oAuth2Info;
    }
}
