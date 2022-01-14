package org.hkyaxhfg.tat.lang.test.json;

import org.hkyaxhfg.tat.lang.json.JSONProcessor;
import org.junit.Test;

/**
 * @author: wjf
 * @date: 2022/1/8
 */
public class JSONProcessorTest {

    private String testField = "123";
    private String name = "wjf";

    private JSONProcessor jsonProcessor = new JSONProcessor();

    @Test
    public void toJsonElement() {
        JSONProcessorTest jsonProcessorTest = new JSONProcessorTest();
        System.out.println(jsonProcessor.toJsonElement(jsonProcessorTest));
    }

    @Test
    public void toJsonString() {
        JSONProcessorTest jsonProcessorTest = new JSONProcessorTest();
        System.out.println(jsonProcessor.toJsonString(jsonProcessorTest));
    }

    @Test
    public void toJavaObject() {
        JSONProcessorTest jsonProcessorTest = jsonProcessor.toJavaObject("{\"testField\":\"123\",\"name\":\"wjf\"}", JSONProcessorTest.class);
        System.out.println(jsonProcessorTest.testField);
        System.out.println(jsonProcessorTest.name);
    }

}
