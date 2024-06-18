package cn.mnay.common.jpa.converter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;


/**
 * 自定义jpa转换器,数据库中用逗号分割，查出时自动变为List
 */
@Converter
public class JpaConverterList implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return CollUtil.isEmpty(attribute) ? null : String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return StrUtil.isBlank(dbData) ? null : Arrays.asList(dbData.split(","));
    }
}
