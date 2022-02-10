package org.hkyaxhfg.tat.lang.json;

import com.google.gson.*;
import org.apache.commons.lang3.time.DateUtils;
import org.hkyaxhfg.tat.lang.util.NewDate;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

/**
 * gson的日期类型序列化和反序列化适配器.
 *
 * @author: wjf
 * @date: 2022/2/10
 */
public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static final String MILLISECOND_PATTEN = "[0-9]+";

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        return new JsonPrimitive(new NewDate(src).formatDateTime());
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
            Class<? extends Date> klass = Unaware.castUnaware(typeOfT);
            if (jsonPrimitive.isNumber()) {
                long dateLong = jsonPrimitive.getAsLong();
                return createDateWithMillisecond(klass, dateLong);
            }
            if (jsonPrimitive.isString()) {
                String dateString = jsonPrimitive.getAsString();
                if (dateString.matches(MILLISECOND_PATTEN)) {
                    return createDateWithMillisecond(klass, Long.parseLong(dateString));
                }
                return createDateWithMillisecond(klass, parseDate(dateString, NewDate.DATE_PATTENS, 0).getTime());
            }
        }
        throw new JsonParseException(String.format("Date 解析错误, type: [%s], value: [%s]", typeOfT.getTypeName(), json.toString()));
    }

    /**
     * 创建日期来自毫秒值.
     * @param klass class.
     * @param millisecond 毫秒值.
     * @return date.
     */
    private Date createDateWithMillisecond(Class<? extends Date> klass, long millisecond) {
        Date date = new Date(millisecond);
        if (klass == NewDate.class) {
            return new NewDate(date);
        }
        return date;
    }

    /**
     * 格式化日期时间.
     * @param dateString 日期字符串.
     * @param datePattens 日期字符串模板字符串数组.
     * @param pattenIndex 当前模板字符串的索引.
     * @return date.
     */
    private static Date parseDate(String dateString, String[] datePattens, int pattenIndex) {
        try {
            return DateUtils.parseDate(dateString, datePattens[pattenIndex]);
        } catch (ParseException pe) {
            if (datePattens.length - 1 > pattenIndex) {
                return parseDate(dateString, datePattens, ++pattenIndex);
            } else {
                throw TatException.newEx("日期格式化错误, dateString: {}", dateString);
            }
        }
    }

}
