package org.hkyaxhfg.tat.enumstrategy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.collections4.CollectionUtils;
import org.hkyaxhfg.tat.lang.reflect.FieldReflector;
import org.hkyaxhfg.tat.lang.util.Pair;

import java.io.IOException;
import java.util.List;

/**
 * jackson对应的枚举策略序列化器.
 *
 * @author: wjf
 * @date: 2022/1/14
 */
@SuppressWarnings("all")
public class JacksonEnumStrategySerializer extends StdSerializer<EnumStrategy> {

    public JacksonEnumStrategySerializer() {
        super(EnumStrategy.class);
    }

    @Override
    public void serialize(EnumStrategy enumStrategy, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString(enumStrategy.name());
        jsonGenerator.writeFieldName("ordinal");
        jsonGenerator.writeNumber(enumStrategy.ordinal());

        EnumSignature enumSignature = EnumSignature.of(enumStrategy.declaringClass());
        List<Pair<FieldReflector, EnumSerialization>> pairs = enumSignature.getPairs();
        if (CollectionUtils.isNotEmpty(pairs)) {
            for (Pair<FieldReflector, EnumSerialization> pair : pairs) {
                FieldReflector fieldReflector = pair.getKey();
                jsonGenerator.writeFieldName(fieldReflector.getFieldName());
                jsonGenerator.writePOJO(fieldReflector.<Object>read(enumStrategy));
            }
        }
        jsonGenerator.writeEndObject();
    }
}
