package org.hkyaxhfg.tat.lang.test.json;

import org.hkyaxhfg.tat.lang.json.JSON;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * @author: wjf
 * @date: 2022/1/8
 */
public class JSONTest {

    @Test
    public void addJsonObject1() {
        JSON json = new JSON();
        json.addJsonObject("name", "wjf");
        json.addJsonObject("age", 18);
        System.out.println(json);
        System.out.println(json.toJavaObject(User.class));
    }

    @Test
    public void addJsonObject2() {
        JSON json = new JSON();
        json.addJsonObject("name", "wjf");
        json.addJsonObject("age", 18);
        System.out.println(json);
        User user = json.toJavaObject(User.class);
        json.addJsonObject("userList", Collections.singleton(user));
        System.out.println(json);
        System.out.println(json.toJavaObject(User.class));
    }

    @Test
    public void addJsonArray() {
        JSON json = new JSON();
        User user = new User();
        user.setName("wjf");
        user.setAge(18);
        json.addJsonArray(user);
        json.addJsonArray(Collections.singleton(user));

        JSON objJson = new JSON();
        objJson.addJsonObject("name", "wjf");
        objJson.addJsonObject("age", 18);

        json.addJsonArray(objJson);
        System.out.println(json);
    }

    @Test
    public void getByJsonKey1() {
        JSON objJson = new JSON();
        objJson.addJsonObject("name", "wjf");
        objJson.addJsonObject("age", 18);

        JSON json = new JSON();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("wjf" + i);
            user.setAge(18 + i);
            json.addJsonArray(user);
        }
        objJson.addJsonObject("userList", json);
        System.out.println(objJson.getByJsonKey("$this.name"));
        System.out.println(objJson.getByJsonKey("$this.age"));
        System.out.println(objJson.getByJsonKey("$this.userList[1]"));
        System.out.println(objJson.getByJsonKey("$this.userList[1].name"));
    }


    @Test
    public void getByJsonKey2() {
        JSON arrayJson = new JSON();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("wjf" + i);
            user.setAge(18 + i);
            arrayJson.addJsonArray(user);
        }

        JSON objJson = new JSON();
        objJson.addJsonObject("name", "wjf");
        objJson.addJsonObject("age", 18);
        arrayJson.addJsonArray(Collections.singleton(objJson));
        arrayJson.addJsonArray(new JSON[] {objJson});

        System.out.println(arrayJson);
        System.out.println(arrayJson.getByJsonKey("$this[0].name"));
        System.out.println(arrayJson.getByJsonKey("$this[1].age"));
        System.out.println(arrayJson.getByJsonKey("$this[5][0]"));
        System.out.println(arrayJson.getByJsonKey("$this[5][0].name"));
        System.out.println(arrayJson.getByJsonKey("$this[6][0].name"));
    }


    public static class User {

        private String name;

        private Integer age;

        private List<User> userList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", userList=" + userList +
                    '}';
        }
    }

}
