package cn.mnay.common.model.wrapper;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapperConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在BaseMapping中定义的default方法，可以在A与B类型转化的时候，针对不同类型的字段自动根据default方法进行转化
 */
@MapperConfig
public interface BaseMapping<A, B> {

    /**
     * 正向转化：<br/>
     * A->B,DBO->DTO
     *
     * @param a A
     * @return B
     */
    B to(A a);

    /**
     * 逆向转化：<br/>
     * B->A,DTO->DBO
     *
     * @param b B
     * @return A
     */
    @InheritInverseConfiguration(
            name = "to"
    )
    A from(B b);

    /**
     * 正向转化：<br/>
     * list形式的DBO->DTO
     *
     * @param a List
     * @return List
     */
    default List<B> to(List<A> a) {
        return a == null ? null : a.stream().map(this::to).collect(Collectors.toList());
    }

    /**
     * 逆向转化：<br/>
     * list形式的DTO->DBO
     *
     * @param b List
     * @return List
     */
    default List<A> from(List<B> b) {
        return b == null ? null : b.stream().map(this::from).collect(Collectors.toList());
    }


    /**
     * Long值的时间戳直接转化为LocalDateTime
     *
     * @param value Long类型值
     * @return LocalDateTime
     */
    default LocalDateTime map(Long value) {
        if (value == null) {
            return null;
        }
        return LocalDateTimeUtil.of(value);
    }

    /**
     * LocalDateTime直接转化为Long值的时间戳
     *
     * @param value LocalDateTime
     * @return Long值的时间戳
     */
    default Long map(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return LocalDateTimeUtil.toEpochMilli(value);
    }


}
