package cn.mnay.common.model.wrapper;

import org.mapstruct.MapperConfig;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 在BaseMapping中定义的default方法，可以在A与B类型转化的时候，针对不同类型的字段自动根据default方法进行转化
 */
@MapperConfig
public interface BaseToMapping <A, B> {

    /**
     * 正向转化：<br/>
     * A->B,DBO->DTO
     *
     * @param a A
     * @return B
     */
    B to(A a);

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

}
