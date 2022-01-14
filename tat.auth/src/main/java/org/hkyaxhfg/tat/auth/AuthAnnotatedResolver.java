package org.hkyaxhfg.tat.auth;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;

/**
 * 鉴权注解解析器, 所有的鉴权注解都应该继承此类.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
public abstract class AuthAnnotatedResolver<Info, AuthAnnotation extends Annotation> implements HandlerMethodArgumentResolver {

    /**
     * 鉴权接口.
     */
    protected Auth<Info> auth;
    /**
     * 对应的注解class.
     */
    private final Class<AuthAnnotation> authAnnotationClass;

    /**
     * 鉴权注解解析器, 需要一个鉴权操作和对应的注解class.
     * @param auth auth.
     * @param authAnnotationClass authAnnotationClass.
     */
    protected AuthAnnotatedResolver(Auth<Info> auth, Class<AuthAnnotation> authAnnotationClass) {
        this.auth = auth;
        this.authAnnotationClass = authAnnotationClass;
    }

    /**
     * 获取鉴权信息.
     * @param authAnnotation 鉴权注解.
     * @return Info.
     */
    public abstract Info getInfo(AuthAnnotation authAnnotation);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(authAnnotationClass);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return getInfo(parameter.getParameterAnnotation(authAnnotationClass));
    }
}
