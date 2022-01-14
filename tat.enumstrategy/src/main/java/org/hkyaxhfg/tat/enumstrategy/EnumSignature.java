package org.hkyaxhfg.tat.enumstrategy;

import org.hkyaxhfg.tat.lang.reflect.ClassReflector;
import org.hkyaxhfg.tat.lang.reflect.FieldReflector;
import org.hkyaxhfg.tat.lang.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 枚举签名, 记录枚举的哪些字段需要被反序列化.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@SuppressWarnings("all")
public class EnumSignature {

    /**
     * 字段反射器枚举键值对列表.
     */
    private List<Pair<FieldReflector, EnumSerialization>> pairs;

    /**
     * 声明此字段的枚举.
     */
    private Class<? extends Enum<?>> declaringClass;

    private EnumSignature() {}

    /**
     * 根据声明class获取枚举签名.
     * @param declaringClass 声明class.
     * @return EnumSignature.
     */
    public static EnumSignature of(Class<? extends Enum<?>> declaringClass) {
        EnumSignature enumSignature = new EnumSignature();
        enumSignature.setDeclaringClass(declaringClass);

        ClassReflector<?> classReflector = new ClassReflector<>(declaringClass);
        List<FieldReflector> fieldReflectors = classReflector.getFieldReflectors();

        List<Pair<FieldReflector, EnumSerialization>> pairs = new ArrayList<>();
        for (FieldReflector fieldReflector : fieldReflectors) {
            if (fieldReflector.hasAnnotation(EnumSerialization.class)) {
                Pair<FieldReflector, EnumSerialization> pair = new Pair<>();
                EnumSerialization enumSerialization = fieldReflector.getAnnotation(EnumSerialization.class);
                pair.setKey(fieldReflector);
                pair.setValue(enumSerialization);
                pairs.add(pair);
            }
        }
        enumSignature.setPairs(pairs);
        return enumSignature;
    }

    public List<Pair<FieldReflector, EnumSerialization>> getPairs() {
        return pairs;
    }

    public void setPairs(List<Pair<FieldReflector, EnumSerialization>> pairs) {
        this.pairs = pairs;
    }

    public Class<? extends Enum<?>> getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(Class<? extends Enum<?>> declaringClass) {
        this.declaringClass = declaringClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnumSignature that = (EnumSignature) o;
        return Objects.equals(declaringClass, that.declaringClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declaringClass);
    }
}
