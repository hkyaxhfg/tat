package org.hkyaxhfg.tat.enumstrategy;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * jackson对应的枚举策略反序列化器.
 *
 * @author: wjf
 * @date: 2022/1/14
 */
@SuppressWarnings("all")
public class JacksonEnumStrategyDeserializer extends JsonDeserializer<EnumStrategy> implements ContextualDeserializer {

    private HashMap<String, Enum<?>> enumConstantMap;

    @Override
    public EnumStrategy deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String enumName = this.enumName(jsonParser);
        return Unaware.castUnaware(this.enumConstantMap.get(enumName));
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        JavaType javaType = beanProperty.getType();
        Class<?> enumClass = javaType.getRawClass();

        if (enumClass.getSuperclass() != Enum.class && !Arrays.asList(enumClass.getInterfaces()).contains(EnumStrategy.class)) {
            TatException.throwEx("[{}]必须是一个枚举, 并且必须实现[{}]接口", enumClass.getTypeName(), EnumStrategy.class.getTypeName());
        }

        Enum<?>[] enumConstants = ((Class<Enum<?>>) enumClass).getEnumConstants();

        if (enumConstants == null) {
            TatException.throwEx("不存在一个className为[{}]的枚举", enumClass.getTypeName());
        }

        HashMap<String, Enum<?>> enumConstantMap = new HashMap<>(enumConstants.length);

        for (Enum<?> enumConstant : enumConstants) {
            enumConstantMap.put(enumConstant.name(), enumConstant);
        }

        JacksonEnumStrategyDeserializer deserializer = new JacksonEnumStrategyDeserializer();
        deserializer.setEnumConstantMap(enumConstantMap);
        return deserializer;
    }

    private String enumName(JsonParser jsonParser) throws IOException {
        JsonToken jsonToken = jsonParser.currentToken();
        String name = null;
        if (jsonToken == JsonToken.START_OBJECT) {
            while (!jsonParser.isClosed() && jsonToken != JsonToken.END_OBJECT) {
                jsonToken = jsonParser.nextToken();
                if (jsonToken == JsonToken.FIELD_NAME
                        && "name".equals(jsonParser.getCurrentName())) {
                    jsonParser.nextToken();
                    name = jsonParser.getValueAsString();
                }
            }
        } else {
            name = jsonParser.getValueAsString();
        }
        return name;
    }

    public HashMap<String, Enum<?>> getEnumConstantMap() {
        return enumConstantMap;
    }

    public void setEnumConstantMap(HashMap<String, Enum<?>> enumConstantMap) {
        this.enumConstantMap = enumConstantMap;
    }
}
