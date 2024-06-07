package cn.mnay.common.enums.base;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class BaseEnumDeSerializer<T extends BaseJsonEnum<T>> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        String filedName = jsonParser.currentName();
        Object currentValue = jsonParser.getParsingContext().getCurrentValue();
        Class<?> aClass = currentValue.getClass();
        Field field = Arrays.stream(aClass.getDeclaredFields()).filter(fi -> StrUtil.equals(filedName, fi.getName())).findFirst()
                .orElseThrow(() -> new RuntimeException("字段名称不匹配"));
        Class<?> type = field.getType();
        if (type.isEnum()) {
            Object[] enumConstants = type.getEnumConstants();
            for (Object enumConstant : enumConstants) {
                if (StrUtil.equals(enumConstant.toString(), jsonParser.getText())) {
                    // noinspection unchecked
                    return (T) enumConstant;
                }
            }
        }
        return null;
    }
}
