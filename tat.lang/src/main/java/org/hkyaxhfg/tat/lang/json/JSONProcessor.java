package org.hkyaxhfg.tat.lang.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.function.Consumer;

/**
 * JSON处理器.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class JSONProcessor {

    /**
     * gson.
     */
    private final Gson gson;

    /**
     * 构造方法, 使用默认的gson.
     */
    public JSONProcessor() {
        this.gson = new Gson();
    }

    /**
     * 构造方法, 使用提供的gson.
     */
    public JSONProcessor(Gson gson) {
        this.gson = gson;
    }

    /**
     * 构造方法, 需要配置一个{@link GsonBuilder}.
     * @param gsonBuilderConsumer gsonBuilderConsumer.
     */
    public JSONProcessor(Consumer<GsonBuilder> gsonBuilderConsumer) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilderConsumer.accept(gsonBuilder);
        this.gson = gsonBuilder.create();
    }

    /**
     * 将对象变成json字符串.
     * @param element 对象.
     * @return json字符串.
     */
    public String toJsonString(Object element) {
        JSON json = new JSON();
        json.setJsonProcessor(this);
        JsonElement jsonElement = json.toJsonElement(element);
        return jsonElement.toString();
    }

    /**
     * 将json字符串变成Java对象.
     * @param jsonString json字符串.
     * @param clazz Java对象class.
     * @param <T> Java对象.
     * @return T.
     */
    public <T> T toJavaObject(String jsonString, Class<T> clazz) {
        return this.gson.fromJson(jsonString, clazz);
    }

    /**
     * 将Java对象转成JsonElement.
     * @param element Java对象.
     * @return JsonElement.
     */
    public JsonElement toJsonElement(Object element) {
        return this.gson.toJsonTree(element);
    }

}
