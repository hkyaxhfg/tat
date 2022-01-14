package org.hkyaxhfg.tat.lang.json;

import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
import org.hkyaxhfg.tat.lang.util.TatException;

import java.util.List;

/**
 * 自定义的json规则.
 *
 * @author: wjf
 * @date: 2022/1/7
 */
interface JsonSyntaxStd {

    /**
     * 解析json字符串为JSON对象.
     * @param json 源json对象.
     * @param key key.
     * @return JSON.
     */
    JSON analyze(JSON json, String key);

    /**
     * 是否包含当前jsonToken.
     * @param key key.
     * @return boolean.
     */
    boolean hasToken(String key);

    /**
     * 获取解析之后的新的jsonKey.
     * @return List<JSONKey>.
     */
    List<JSONKey> getNewKeys();

    /**
     * json-token.
     */
    enum JsonToken implements JsonSyntaxStd {
        THIS("$this") {
            @Override
            public JSON analyze(JSON json, String key) {
                if (StringUtils.isBlank(key) || !this.hasToken(key)) {
                    throw TatException.newEx("json-key: [{}] 必须以 [$this] 开始", key);
                }
                List<JSONKey> jsonKeys = JSONKey.analyze(key);
                JSONKey firstKey = jsonKeys.get(0);
                if (firstKey.getJsonType() != json.getJsonType()) {
                    throw TatException.newEx("jsonType类型不一致, 源jsonType: [{}], 解析的jsonType: [{}]", json.getJsonType().name(), firstKey.getJsonType().name());
                }
                this.keys = jsonKeys.subList(1, jsonKeys.size());
                switch (firstKey.getJsonType()) {
                    case OBJECT:
                        // $this
                        return json;
                    case ARRAY:
                        // $this[index1][index2][...]
                        return new JSON(getArrayChildren(json.getJsonElement(), firstKey.getIndexes(), 0));
                    default:
                        throw TatException.newEx("不支持的 jsonType: [{}]", firstKey.getJsonType().name());
                }
            }

            @Override
            public boolean hasToken(String key) {
                return key.trim().startsWith(this.token);
            }

            @Override
            public List<JSONKey> getNewKeys() {
                return this.keys;
            }

        };

        final String token;

        List<JSONKey> keys;

        JsonToken(String token) {
            this.token = token;
        }

        /**
         * 递归获取数组的值.
         * @param parentNode 父节点.
         * @param indexes 索引数组.
         * @param index 当前indexes的索引值.
         * @return JsonElement.
         */
        public static JsonElement getArrayChildren(JsonElement parentNode, int[] indexes, int index) {
            JsonElement currentNode = parentNode.getAsJsonArray().get(indexes[index]);
            if (index == indexes.length - 1) {
                return currentNode;
            }
            return getArrayChildren(currentNode, indexes, index + 1);
        }

    }

}
