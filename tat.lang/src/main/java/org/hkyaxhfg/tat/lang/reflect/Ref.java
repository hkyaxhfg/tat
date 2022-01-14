package org.hkyaxhfg.tat.lang.reflect;

import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类型token-ref.
 *
 * @param <T> {@link T}.
 *
 * @author: wjf
 * @date: 2021/9/28 15:13
 */
public class Ref<T> {

    /**
     * 泛型的实际类型class.
     */
    private final Class<T> clazz;

    /**
     * protected的构造方法，需要子类实现.
     */
    protected Ref() {
        Type superclass = this.getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            this.clazz = Unaware.castUnaware(Unaware.<ParameterizedType>castUnaware(superclass).getActualTypeArguments()[0]);
        } else {
            throw TatException.newEx("The generic type of Ref<T> is not generalized");
        }
    }

    /**
     * 获取Class<T>.
     *
     * @return Class<T>.
     */
    public Class<T> getClazz() {
        return this.clazz;
    }

    /**
     * 通过无参构造创建一个T对象, 如果不存在无参构造则抛出异常.
     * @param constructorParameterTypes 构造器参数类型.
     * @param constructorParameters 构造器参数.
     * @return T.
     */
    public T newInstance(Class<?>[] constructorParameterTypes, Object[] constructorParameters) {
        ClassReflector<T> classReflector = new ClassReflector<>(this.clazz);
        return classReflector.constructor(constructorParameterTypes).newInstance(constructorParameters);
    }

}
