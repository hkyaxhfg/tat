package org.hkyaxhfg.tat.lang.test.reflect;

import org.hkyaxhfg.tat.lang.reflect.ClassReflector;
import org.hkyaxhfg.tat.lang.reflect.FieldReflector;
import org.hkyaxhfg.tat.lang.reflect.MethodReflector;
import org.junit.Test;

/**
 * @author: wjf
 * @date: 2022/1/7
 */
public class ClassReflectorTest {

    private String testField;

    @Test
    public void toReflectName() {
        ClassReflector<ClassReflectorTest> classReflector = new ClassReflector<ClassReflectorTest>(ClassReflectorTest.class);
        System.out.println(classReflector.toReflectName());
    }

    @Test
    public void newInstance() {
        ClassReflector<ClassReflectorTest> classReflector = new ClassReflector<ClassReflectorTest>(ClassReflectorTest.class);
        System.out.println(classReflector.constructor().newInstance());
    }

    @Test
    public void getMethodReflectors() {
        ClassReflector<ClassReflectorTest> classReflector = new ClassReflector<ClassReflectorTest>(ClassReflectorTest.class);
        for (MethodReflector methodReflector : classReflector.getMethodReflectors()) {
            System.out.println(methodReflector.toReflectName());
        }
    }

    @Test
    public void getMethodReflector() {
        ClassReflector<ClassReflectorTest> classReflector = new ClassReflector<ClassReflectorTest>(ClassReflectorTest.class);
        MethodReflector methodReflector = classReflector.getMethodReflector("getMethodReflectors");
        System.out.println(methodReflector.toReflectName());
    }

    @Test
    public void getFieldReflectors() {
        ClassReflector<ClassReflectorTest> classReflector = new ClassReflector<ClassReflectorTest>(ClassReflectorTest.class);
        for (FieldReflector fieldReflector : classReflector.getFieldReflectors()) {
            System.out.println(fieldReflector.toReflectName());
        }
    }

    @Test
    public void getFieldReflector() {
        ClassReflector<ClassReflectorTest> classReflector = new ClassReflector<ClassReflectorTest>(ClassReflectorTest.class);
        FieldReflector fieldReflector = classReflector.getFieldReflector("testField");
        System.out.println(fieldReflector.toReflectName());
    }

}
