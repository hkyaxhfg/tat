package org.hkyaxhfg.tat.lang.json;

import com.google.gson.*;
import org.hkyaxhfg.tat.lang.util.Array;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.util.Collection;
import java.util.List;

/**
 * json实体.
 *
 * @author: wjf
 * @date: 2022/1/7
 */
public class JSON {

    /**
     * jsonElement.
     */
    private JsonElement jsonElement;

    /**
     * jsonProcessor.
     */
    private JSONProcessor jsonProcessor;

    /**
     * jsonType.
     */
    private JsonType jsonType;

    /**
     * 构造方法.
     */
    public JSON() {
        this(Unaware.<JSON>castUnaware(null));
    }

    /**
     * 构造方法, 需要传入一个{@link JSON}对象.
     * @param json json.
     */
    public JSON(JSON json) {
        this(json != null ? json.jsonElement : null, new JSONProcessor());
    }

    /**
     * 构造方法, 需要传入一个{@link JsonElement}对象.
     * @param jsonElement jsonElement.
     */
    public JSON(JsonElement jsonElement) {
        this(jsonElement, new JSONProcessor());
    }

    /**
     * 构造方法, 需要传入一个{@link JsonElement}和{@link JSONProcessor}对象.
     * @param jsonElement JsonElement.
     * @param jsonProcessor JSONProcessor.
     */
    public JSON(JsonElement jsonElement, JSONProcessor jsonProcessor) {
        if (jsonElement != null && !(jsonElement instanceof JsonObject) && !(jsonElement instanceof JsonArray)) {
            throw TatException.newEx("jsonElement 必须为 null 或者 JsonObject 或者 JsonArray");
        }
        this.jsonElement = jsonElement;
        this.jsonProcessor = jsonProcessor;
    }

    /**
     * 构造方法, 需要传入一个{@link JSON}和{@link JSONProcessor}对象.
     * @param json JSON.
     * @param jsonProcessor JSONProcessor.
     */
    public JSON(JSON json, JSONProcessor jsonProcessor) {
        this(json != null ? json.jsonElement : null, jsonProcessor);
    }

    /**
     * 添加JsonObject属性.
     * @param name 属性名称.
     * @param value 属性值.
     * @return JSON.
     */
    public JSON addJsonObject(String name, Object value) {
        JsonObject jsonObject = this.initRootNode(JsonType.OBJECT);
        jsonObject.add(name, this.toJsonElement(value));
        return this;
    }

    /**
     * 添加JsonArray属性.
     * @param value 属性值.
     * @return JSON.
     */
    public JSON addJsonArray(Object value) {
        JsonArray jsonArray = this.initRootNode(JsonType.ARRAY);
        jsonArray.add(this.toJsonElement(value));
        return this;
    }

    /**
     * 通过json-key获取数据, 只支持json-key规则获取数据.
     * json-key规则如下:
     * 1. 获取user对象的list属性的第一个元素 -> $this.list[0]
     * 2. 获取user对象的name属性 -> $this.name
     * 3. 获取list列表的第一个元素 -> $this[0]
     * 4. 获取list列表的第一个元素的name属性 -> $this[0].name
     * 5. 获取list列表的第一个元素(列表的第一个元素也是列表)的二个元素的name属性 -> $this[0][1].name
     * @param key 符合json-key模式的key.
     * @return JSON.
     */
    @SuppressWarnings("all")
    public JSON getByJsonKey(String key) {
        JsonSyntaxStd.JsonToken $this = JsonSyntaxStd.JsonToken.THIS;
        JSON json = $this.analyze(this, key);
        List<JSONKey> keys = $this.getNewKeys();
        if (keys.isEmpty()) {
            return json;
        }
        JSON result = new JSON();
        result.setJsonElement(getValueByKeys(json.getJsonElement(), keys, 0));
        return result;
    }

    /**
     * 将当前JSON变成Java对象.
     * @param clazz class.
     * @param <T> T.
     * @return T.
     */
    public <T> T toJavaObject(Class<T> clazz) {
        JsonElement element = this.jsonElement == null ? JsonNull.INSTANCE : this.jsonElement;
        return jsonProcessor.toJavaObject(element.toString(), clazz);
    }

    /**
     * 获取jsonType.
     * @return JsonType.
     */
    public JsonType getJsonType() {
        return jsonType;
    }

    /**
     * 设置jsonType, 此方法限制外部使用, 只在当前包内部使用.
     * @param jsonType
     */
    void setJsonType(JsonType jsonType) {
        this.jsonType = jsonType;
    }

    /**
     * 获取jsonProcessor.
     * @return JSONProcessor.
     */
    public JSONProcessor getJsonProcessor() {
        return jsonProcessor;
    }

    /**
     * 设置jsonProcessor, 此方法限制外部使用, 只在当前包内部使用.
     * @param jsonProcessor
     */
    void setJsonProcessor(JSONProcessor jsonProcessor) {
        this.jsonProcessor = jsonProcessor;
    }

    /**
     * 获取jsonElement.
     * @return JsonElement.
     */
    public JsonElement getJsonElement() {
        return jsonElement;
    }

    /**
     * 设置jsonElement, 此方法限制外部使用, 只在当前包内部使用.
     * @param jsonElement
     */
    void setJsonElement(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }

    @Override
    public String toString() {
        return this.jsonElement == null ? JsonNull.INSTANCE.toString() : this.jsonElement.toString();
    }

    /**
     * 递归获取符合json-key的数据.
     * @param parentNode 父节点.
     * @param jsonKeys json-keys.
     * @param index json-keys的当前索引.
     * @return JsonElement.
     */
    private JsonElement getValueByKeys(JsonElement parentNode, List<JSONKey> jsonKeys, int index) {
        // 获取当前json-key
        JSONKey jsonKey = jsonKeys.get(index);
        // 当前节点
        JsonElement currentNode = null;
        // 当前节点为JsonObject
        if (parentNode instanceof JsonObject) {
            JsonObject jsonObject = Unaware.castUnaware(parentNode);
            switch (jsonKey.getJsonType()) {
                // 解析的json-key为对象类型, 直接获取数据
                case OBJECT:
                    currentNode = jsonObject.get(jsonKey.getSubKey());
                    break;
                // 解析的json-key为数组类型, 递归获取数据
                case ARRAY:
                    currentNode = JsonSyntaxStd.JsonToken.getArrayChildren(jsonObject.get(jsonKey.getSubKey()), jsonKey.getIndexes(), 0);
                    break;
                default:
                    throw TatException.newEx("json-key-type不能为空");
            }
        }
        // 当前节点为JsonArray
        if (parentNode instanceof JsonArray) {
            JsonArray jsonArray = Unaware.castUnaware(parentNode);
            switch (jsonKey.getJsonType()) {
                // 解析的json-key为数组类型, 递归获取数据
                case ARRAY:
                    currentNode = JsonSyntaxStd.JsonToken.getArrayChildren(jsonArray, jsonKey.getIndexes(), 0);
                    break;
                // 不支持
                case OBJECT:
                default:
                    throw TatException.newEx("json-type解析错误, 当前类型: {}, 解析类型: {}", "Array", "Object");
            }
        }
        // 不支持
        if (parentNode instanceof JsonPrimitive) {
            throw TatException.newEx("json-type解析错误, 不支持的解析类型: {}", "Primitive");
        }
        // 不支持
        if (parentNode instanceof JsonNull) {
            throw TatException.newEx("json-type解析错误, 不支持的解析类型: {}", "Null");
        }
        // 当拿到key的最后一层时返回
        if (index == jsonKeys.size() - 1) {
            return currentNode;
        }
        // 不是最后一层递归获取数据
        return getValueByKeys(currentNode, jsonKeys, index + 1);
    }

    /**
     * 初始化root节点.
     * @param jsonType root节点的json类型.
     * @param <T> T.
     * @return T.
     */
    private <T> T initRootNode(JsonType jsonType) {
        if (this.jsonElement == null) {
            this.jsonType = jsonType;
            switch (jsonType) {
                case OBJECT:
                    this.jsonElement = new JsonObject();
                    break;
                case ARRAY:
                    this.jsonElement = new JsonArray();
                    break;
                default:
                    throw TatException.newEx("不支持的jsonType: {}", jsonType.name());
            }
        }
        return Unaware.castUnaware(this.jsonElement);
    }

    /**
     * 将任意对象转化成JsonElement, 此方法限制外部使用, 只在当前包内部使用.
     * @param value 任意对象.
     * @return JsonElement.
     */
    JsonElement toJsonElement(Object value) {
        if (value == null) {
            return JsonNull.INSTANCE;
        }
        if (value instanceof JSON) {
            return Unaware.<JSON>castUnaware(value).jsonElement;
        }
        if (value instanceof JsonElement) {
            return Unaware.castUnaware(value);
        }
        if (value.getClass().isArray()) {
            JsonArray jsonArray = new JsonArray();
            Array<Object> array = new Array<>(value);
            while (array.hasNext()) {
                Object element = array.next();
                jsonArray.add(toJsonElement(element));
            }
            return jsonArray;
        }
        if (value instanceof Collection) {
            JsonArray jsonArray = new JsonArray();
            Collection<Object> collection = Unaware.castUnaware(value);
            for (Object element : collection) {
                jsonArray.add(toJsonElement(element));
            }
            return jsonArray;
        }
        return this.jsonProcessor.toJsonElement(value);
    }

    /**
     * JsonType
     */
    enum JsonType {
        OBJECT,
        ARRAY
    }

}
