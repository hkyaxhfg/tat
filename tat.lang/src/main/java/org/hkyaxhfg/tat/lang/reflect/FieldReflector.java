package org.hkyaxhfg.tat.lang.reflect;

import org.hkyaxhfg.tat.lang.util.Container;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 字段反射器, 用于字段的反射.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class FieldReflector implements Reflector {

    /**
     * 目标class.
     */
    private final Class<?> clazz;

    /**
     * 字段.
     */
    private Field field;

    /**
     * 字段名称.
     */
    private final String fieldName;

    /**
     * 构造方法.
     * @param field 字段.
     */
    public FieldReflector(Field field) {
        this.field = field;
        this.clazz = this.field.getDeclaringClass();
        this.fieldName = this.field.getName();
    }

    /**
     * 构造方法.
     * @param fieldName 字段名称.
     * @param clazz 目标class.
     */
    public FieldReflector(String fieldName, Class<?> clazz) {
        this.fieldName = fieldName;
        this.clazz = clazz;
        Unaware.exceptionUnaware(() -> this.field = this.clazz.getDeclaredField(this.fieldName));
    }

    @Override
    public <A extends Annotation> boolean hasAnnotation(Class<A> clazz) {
        return this.field.isAnnotationPresent(clazz);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> clazz) {
        return this.field.getAnnotation(clazz);
    }

    @Override
    public String toReflectName() {
        return this.field.toGenericString();
    }

    /**
     * 获取字段的值.
     * @param target 目标.
     * @param <T> 值的类型.
     * @return 值.
     */
    @SuppressWarnings("all")
    public <T> T read(Object target) {
        Container<T> container = new Container<>();
        Unaware.exceptionUnaware(() -> {
            boolean accessible = this.field.isAccessible();
            if (!accessible) {
                this.field.setAccessible(true);
            }
            container.put(Unaware.castUnaware(this.field.get(target)));
            this.field.setAccessible(accessible);
        });
        return container.get();
    }

    /**
     * 写入字段的值.
     * @param target 目标.
     * @param value 目标值.
     * @param <T> 值的类型.
     */
    @SuppressWarnings("all")
    public <T> void write(Object target, T value) {
        Unaware.exceptionUnaware(() -> {
            boolean accessible = this.field.isAccessible();
            if (!accessible) {
                this.field.setAccessible(true);
            }
            this.field.set(target, value);
            this.field.setAccessible(accessible);
        });
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Field getField() {
        return field;
    }

    public String getFieldName() {
        return fieldName;
    }
}
