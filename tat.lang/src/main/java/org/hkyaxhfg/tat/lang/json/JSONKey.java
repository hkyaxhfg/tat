package org.hkyaxhfg.tat.lang.json;

import org.apache.commons.lang3.StringUtils;
import org.hkyaxhfg.tat.lang.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * JsonKey, 用来解析jsonKey.
 *
 * @author: wjf
 * @date: 2022/1/7
 */
public class JSONKey {

    /**
     * 数组前缀.
     */
    private static final String arrayPrefix = "[";

    /**
     * 数组后缀.
     */
    private static final String arraySuffix = "]";

    /**
     * 子key.
     */
    private final String subKey;

    /**
     * jsonType.
     */
    private final JSON.JsonType jsonType;

    /**
     * 当jsonType为数组时, 即是索引值数组(存在取值为[1][2]的情况), 为对象时, 即是int[0].
     */
    private final int[] indexes;

    /**
     * 构造方法, 需要传入子key, jsonType, index.
     * @param subKey 子key.
     * @param jsonType jsonType.
     * @param indexes indexes.
     */
    public JSONKey(String subKey, JSON.JsonType jsonType, int[] indexes) {
        this.subKey = subKey;
        this.jsonType = jsonType;
        this.indexes = indexes;
    }

    /**
     * 解析主key, 返回子key列表.
     * @param mainKey 主key.
     * @return List<JSONKey>.
     */
    public static List<JSONKey> analyze(String mainKey) {
        if (StringUtils.isBlank(mainKey)) {
            return Collections.emptyList();
        }
        List<JSONKey> keys = new ArrayList<>();
        Arrays.stream(mainKey.split("\\."))
                .map(String::trim)
                .forEach(
                        subKey -> keys.add(isJsonArray(subKey) ?
                                assemblyJsonArray(subKey) :
                                new JSONKey(subKey, JSON.JsonType.OBJECT, new int[0]))
                );
        return keys;
    }

    /**
     * 判断当前子key是否为数组.
     * @param subKey 子key.
     * @return boolean.
     */
    private static boolean isJsonArray(String subKey) {
        return subKey.contains(arrayPrefix) && subKey.contains(arraySuffix) && subKey.endsWith(arraySuffix);
    }

    /**
     * 判断当前pattern是否是数组前缀.
     * @param pattern pattern.
     * @return boolean.
     */
    private static boolean isArrayPrefix(String pattern) {
        return arrayPrefix.equals(pattern);
    }

    /**
     * 判断当前pattern是否是数组后缀.
     * @param pattern pattern.
     * @return boolean.
     */
    private static boolean isArraySuffix(String pattern) {
        return arraySuffix.equals(pattern);
    }

    /**
     * 当为数组时, 组装JsonArray.
     * @param subKey subKey.
     * @return JSONKey.
     */
    private static JSONKey assemblyJsonArray(String subKey) {
        // 第一个[的索引位置
        int arrayPrefixIndex = subKey.indexOf(arrayPrefix);
        // 第一个[之前的字符串就是key
        String key = subKey.substring(0, arrayPrefixIndex);
        // 获取从第一个[开始的所有字符串
        String arrayPatterns = subKey.substring(arrayPrefixIndex);
        // 待解析的字符数组
        char[] toBeResolved = arrayPatterns.toCharArray();

        // 创建Pair列表, 用来记录一对[]的索引位置, key = [, value = ]
        List<Pair<Integer, Integer>> toBeResolvedRange = new ArrayList<>();
        // 遍历字符数组
        for (int i = 0; i < toBeResolved.length; i++) {
            String toBe = String.valueOf(toBeResolved[i]);
            // 当为 [ 或者 ] 时才进行操作
            if (isArrayPrefix(toBe) || isArraySuffix(toBe)) {
                // 判断是否为[
                if (isArrayPrefix(toBe)) {
                    Pair<Integer, Integer> arrayPair = new Pair<>();
                    // 存放[的索引值
                    arrayPair.setKey(i);
                    toBeResolvedRange.add(arrayPair);
                }
                // 判断是否为]
                if (isArraySuffix(toBe)) {
                    Pair<Integer, Integer> arrayPair = toBeResolvedRange.get(toBeResolvedRange.size() - 1);
                    // 存放]的索引值
                    arrayPair.setValue(i);
                }

            }
        }

        // 最终的数组取值方式的索引
        int[] indexes = new int[toBeResolvedRange.size()];

        // 遍历pairs
        for (int i = 0; i < toBeResolvedRange.size(); i++) {
            Pair<Integer, Integer> pair = toBeResolvedRange.get(i);
            // 解析数组索引
            indexes[i] = Integer.parseInt(arrayPatterns.substring(pair.getKey() + 1, pair.getValue()));
        }
        return new JSONKey(key, JSON.JsonType.ARRAY, indexes);
    }

    public String getSubKey() {
        return subKey;
    }

    public JSON.JsonType getJsonType() {
        return jsonType;
    }

    public int[] getIndexes() {
        return indexes;
    }
}
