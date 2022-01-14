package org.hkyaxhfg.tat.enumstrategy;

import com.google.gson.*;
import org.apache.commons.collections4.CollectionUtils;
import org.hkyaxhfg.tat.lang.reflect.FieldReflector;
import org.hkyaxhfg.tat.lang.util.Pair;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

/**
 * gson的枚举类型序列化和反序列化适配器.
 *
 * @author: wjf
 * @date: 2022/1/11
 */

public class EnumTypeAdapter implements JsonSerializer<EnumStrategy<?>>, JsonDeserializer<EnumStrategy<?>> {

    @Override
    public JsonElement serialize(EnumStrategy<?> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.name());
        jsonObject.addProperty("ordinal", src.ordinal());

        EnumSignature enumSignature = EnumSignature.of(src.declaringClass());
        List<Pair<FieldReflector, EnumSerialization>> pairs = enumSignature.getPairs();
        if (CollectionUtils.isNotEmpty(pairs)) {
            for (Pair<FieldReflector, EnumSerialization> pair : pairs) {
                caseEnumFieldType(pair.getKey(), pair.getValue(), src, jsonObject);
            }
        }
        return jsonObject;
    }

    @Override
    @SuppressWarnings("all")
    public EnumStrategy<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(typeOfT instanceof Class<?>) && Unaware.<Class<?>>castUnaware(typeOfT).getSuperclass() != Enum.class) {
            throw TatException.newEx("{}不是一个Enum<?>", Unaware.<Class<?>>castUnaware(typeOfT).getSuperclass().getTypeName());
        }
        Class<? extends Enum> enumClazz = Unaware.<Class<? extends Enum>>castUnaware(typeOfT);
        return Unaware.<EnumStrategy<?>>castUnaware(Enum.valueOf(enumClazz, json.getAsString()));
    }

    /**
     * 捕捉枚举字段类型, 并将数据放入jsonObject.
     *
     * @param fieldReflector    字段反射器.
     * @param enumSerialization EnumSerialization.
     * @param enumStrategy      EnumStrategy.
     * @param jsonObject        JsonObject.
     */

    private void caseEnumFieldType(FieldReflector fieldReflector, EnumSerialization enumSerialization, EnumStrategy<?> enumStrategy, JsonObject jsonObject) {
        String fieldName = fieldReflector.getFieldName();
        Class<?> fieldType = fieldReflector.getField().getType();

        if (fieldType == String.class) {
            jsonObject.addProperty(fieldName, fieldReflector.<String>read(enumStrategy));
        } else if (fieldType == int.class || fieldType == Integer.class) {
            jsonObject.addProperty(fieldName, fieldReflector.<Integer>read(enumStrategy));
        } else if (fieldType == long.class || fieldType == Long.class) {
            jsonObject.addProperty(fieldName, fieldReflector.<Long>read(enumStrategy));
        } else if (fieldType == BigDecimal.class) {
            jsonObject.addProperty(fieldName, fieldReflector.<BigDecimal>read(enumStrategy));
        } else {
            throw TatException.newEx("目前只支持 [int, Integer, long, Long, String, BigDecimal] 类型");
        }
    }

}
