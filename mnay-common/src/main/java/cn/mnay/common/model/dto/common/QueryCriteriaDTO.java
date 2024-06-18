package cn.mnay.common.model.dto.common;

import cn.mnay.common.annotation.JPASpecification;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QueryCriteriaDTO {
    private String fieldName;
    // 单体查询条件使用fieldValue
    private String fieldValue;
    // 区别是单体还是区间:true->单体；false->区间
    private Boolean single;
    private JPASpecification singleJpaSpecification;
    // 区间查询条件使用startValue和endValue
    private String startValue;
    private String endValue;
    private JPASpecification startJpaSpecification;
    private JPASpecification endJpaSpecification;
}
