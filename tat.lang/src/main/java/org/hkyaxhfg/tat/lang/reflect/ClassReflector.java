package org.hkyaxhfg.tat.lang.reflect;

import org.hkyaxhfg.tat.lang.util.Container;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类反射器, 用于类的反射.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class ClassReflector<T> implements Reflector {

    /**
     * 目标class.
     */
    private Class<T> clazz;

    /**
     * 目标构造方法, 有可能为null.
     */
    private Constructor<T> constructor;

    /**
     * 构造方法.
     * @param clazz clazz.
     */
    public ClassReflector(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 构造方法.
     * @param className className.
     */
    public ClassReflector(String className) {
        Unaware.exceptionUnaware(() -> this.clazz = Unaware.castUnaware(Class.forName(className)));
    }

    @Override
    public <A extends Annotation> boolean hasAnnotation(Class<A> clazz) {
        return this.clazz.isAnnotationPresent(clazz);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> clazz) {
        return this.clazz.getAnnotation(clazz);
    }

    @Override
    public String toReflectName() {
        return this.clazz.toGenericString();
    }

    /**
     * 获取对应的构造方法.
     * @param constructorParameterTypes 构造器参数类型列表.
     * @return 构造方法.
     */
    public ClassReflector<T> constructor(Class<?>... constructorParameterTypes) {
        Unaware.exceptionUnaware(() -> this.constructor = this.clazz.getConstructor(constructorParameterTypes));
        return this;
    }

    /**
     * 根据构造器创建对象.
     * @param constructorParameters 构造器所需参数列表.
     * @return 对象.
     */
    @SuppressWarnings("all")
    public T newInstance(Object... constructorParameters) {
        Container<T> container = new Container<>();
        Unaware.exceptionUnaware(() -> {
            boolean accessible = this.constructor.isAccessible();
            if (!accessible) {
                this.constructor.setAccessible(true);
            }
            container.put(this.constructor.newInstance(constructorParameters));
            this.constructor.setAccessible(accessible);
        });
        return container.get();
    }

    /**
     * 获取所有的方法反射器.
     * @return List<MethodReflector>.
     */
    public List<MethodReflector> getMethodReflectors() {
        Method[] methods = this.clazz.getMethods();
        if (methods.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(methods).map(MethodReflector::new).collect(Collectors.toList());
    }

    /**
     * 获取某个方法反射器.
     * @param methodName 方法名称.
     * @param methodParameterTypes 方法的参数类型列表.
     * @return MethodReflector.
     */
    public MethodReflector getMethodReflector(String methodName, Class<?>... methodParameterTypes) {
        return new MethodReflector(this.clazz, methodName, methodParameterTypes);
    }

    /**
     * 获取所有的字段反射器.
     * @return List<FieldReflector>.
     */
    public List<FieldReflector> getFieldReflectors() {
        List<Field> fields = new ArrayList<>(Arrays.asList(this.clazz.getDeclaredFields()));
        Class<?> superClass = this.clazz;

        while (superClass != Object.class) {
            superClass = superClass.getSuperclass();
            fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
        }

        if (fields.isEmpty()) {
            return Collections.emptyList();
        }
        return fields.stream().map(FieldReflector::new).collect(Collectors.toList());
    }

    /**
     * 获取某个字段反射器.
     * @param fieldName 字段名称.
     * @return MethodReflector.
     */
    public FieldReflector getFieldReflector(String fieldName) {
        return new FieldReflector(fieldName, this.clazz);
    }



}
