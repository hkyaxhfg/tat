package org.hkyaxhfg.tat.lang.test.reflect;

import org.hkyaxhfg.tat.lang.reflect.MethodReflector;
import org.junit.Test;

import java.lang.reflect.Parameter;

/**
 * @author: wjf
 * @date: 2022/1/7
 */
public class MethodReflectorTest {

    @Test
    public void toReflectName() {
        MethodReflector methodReflector = new MethodReflector(MethodReflectorTest.class, "toReflectName", String.class);
        System.out.println(methodReflector.toReflectName());
    }

    @Test
    public void invoke() {
        MethodReflector methodReflector = new MethodReflector(MethodReflectorTest.class, "toReflectName", String.class);
        methodReflector.invoke(null, "123");
    }

    @Test
    public void getParameters() {
        MethodReflector methodReflector = new MethodReflector(MethodReflectorTest.class, "toReflectName", String.class);
        for (Parameter parameter : methodReflector.getParameters()) {
            System.out.println(parameter.getName());
        }
    }

    public static void toReflectName(String name) {
        MethodReflector methodReflector = new MethodReflector(MethodReflectorTest.class, "toReflectName");
        System.out.println(methodReflector.toReflectName());
        System.out.println(name);
    }

}
