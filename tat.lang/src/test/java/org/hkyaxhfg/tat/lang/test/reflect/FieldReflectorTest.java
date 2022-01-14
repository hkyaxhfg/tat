package org.hkyaxhfg.tat.lang.test.reflect;

import org.hkyaxhfg.tat.lang.reflect.FieldReflector;
import org.junit.Test;

/**
 * @author: wjf
 * @date: 2022/1/8
 */
public class FieldReflectorTest {

    private static String testField = "123";

    @Test
    public void toReflectName() {
        FieldReflector classReflector = new FieldReflector("testField", FieldReflectorTest.class);
        System.out.println(classReflector.toReflectName());
    }

    @Test
    public void read() {
        FieldReflector classReflector = new FieldReflector("testField", FieldReflectorTest.class);
        System.out.println(classReflector.<String>read(null));
    }

    @Test
    public void write() {
        FieldReflector classReflector = new FieldReflector("testField", FieldReflectorTest.class);
        classReflector.write(null, "456");
        read();
    }

}
