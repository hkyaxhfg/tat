package org.hkyaxhfg.tat.lang.reflect;

import org.hkyaxhfg.tat.lang.util.Container;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 方法反射器, 用于方法的反射.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class MethodReflector implements Reflector {

    /**
     * 目标class.
     */
    private final Class<?> clazz;

    /**
     * 方法名称.
     */
    private final String methodName;

    /**
     * 方法参数类型列表.
     */
    private final Class<?>[] methodParameterTypes;

    /**
     * 方法.
     */
    private Method method;

    /**
     * 构造方法.
     * @param method 方法.
     */
    public MethodReflector(Method method) {
        this.method = method;
        this.clazz = this.method.getDeclaringClass();
        this.methodName = this.method.getName();
        this.methodParameterTypes = this.method.getParameterTypes();
    }

    /**
     * 构造方法.
     * @param clazz 目标class.
     * @param methodName 方法名称.
     * @param methodParameterTypes 方法参数类型列表.
     */
    public MethodReflector(Class<?> clazz, String methodName, Class<?>... methodParameterTypes) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.methodParameterTypes = methodParameterTypes;
        Unaware.exceptionUnaware(() -> this.method = this.clazz.getMethod(this.methodName, this.methodParameterTypes));
    }

    @Override
    public <A extends Annotation> boolean hasAnnotation(Class<A> clazz) {
        return this.method.isAnnotationPresent(clazz);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> clazz) {
        return this.method.getAnnotation(clazz);
    }

    @Override
    public String toReflectName() {
        return this.method.toGenericString();
    }

    /**
     * 获取方法的运行结果.
     * @param target 目标.
     * @param methodParameters 参数列表.
     * @param <T> 运行结果的类型.
     * @return 运行结果.
     */
    @SuppressWarnings("all")
    public <T> T invoke(Object target, Object... methodParameters) {
        Container<T> container = new Container<>();
        Unaware.exceptionUnaware(() -> {
            boolean accessible = this.method.isAccessible();
            if (!accessible) {
                this.method.setAccessible(true);
            }
            container.put(Unaware.castUnaware(this.method.invoke(target, methodParameters)));
            this.method.setAccessible(accessible);
        });
        return container.get();
    }

    /**
     * 获取方法的所有参数.
     * @return List<Parameter>.
     */
    public List<Parameter> getParameters() {
        return Arrays.stream(this.method.getParameters()).collect(Collectors.toList());
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getMethodParameterTypes() {
        return methodParameterTypes;
    }

    public Method getMethod() {
        return method;
    }
}
