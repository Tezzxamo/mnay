package cn.mnay.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.mnay.common.annotation.JPAExampleMatcher;
import cn.mnay.common.annotation.JPASort;
import cn.mnay.common.annotation.JPASpecification;
import cn.mnay.common.model.dto.common.BaseCriteria;
import cn.mnay.common.model.dto.common.QueryCriteriaDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JPAUtils {

    /**
     * 通过BaseCriteria中的排序条件组装「排序对象」，如果没有排序条件，则返回无排序设置的Sort
     *
     * @param criteria 查询条件
     * @return 排序对象
     * @throws IllegalAccessException 非法访问异常(此处为获取)
     */
    public static Sort sortFromCriteria(BaseCriteria criteria) throws IllegalAccessException {
        if (criteria == null) {
            return Sort.unsorted();
        }
        Class<? extends BaseCriteria> criteriaClass = criteria.getClass();
        Field[] fields = criteriaClass.getDeclaredFields();
        List<Sort.Order> orderList = new ArrayList<>();
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            // 对标注了JPASort注解的字段且字段值不为null的，进行Order拼装
            if (field.isAnnotationPresent(JPASort.class) && Objects.nonNull(field.get(criteria))) {
                JPASort jpaSort = field.getAnnotation(JPASort.class);
                boolean desc = (Boolean) field.get(criteria);
                Sort.Order order = desc ? Sort.Order.desc(jpaSort.filedName()) : Sort.Order.asc(jpaSort.filedName());
                orderList.add(order);
            }
        }
        if (CollUtil.isNotEmpty(orderList)) {
            return Sort.by(orderList);
        }
        return Sort.unsorted();
    }


    /**
     * 通过BaseCriteria中example部分的字段标注的注解 @JPAExampleMatcher 组装ExampleMatcher
     * 注意：灵活匹配只针对于String类型，其余类型只能精确匹配，所以完全动态的查询ExampleMatcher并不支持
     *
     * @param criteria 查询条件
     * @return ExampleMatcher
     */
    public static ExampleMatcher exampleMatcherFromCriteria(BaseCriteria criteria) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase();
        if (criteria == null) {
            return matcher;
        }
        Class<? extends BaseCriteria> criteriaClass = criteria.getClass();
        Field[] fields = criteriaClass.getDeclaredFields();
        for (Field field : fields) {
            // 对标注了JPAExampleMatcher注解的字段，进行matcher拼装
            if (field.isAnnotationPresent(JPAExampleMatcher.class)) {
                JPAExampleMatcher jpaExampleMatcher = field.getAnnotation(JPAExampleMatcher.class);
                // 根据标注的不同查询条件，进行匹配规则
                if (jpaExampleMatcher.fuzzyQuery()) {
                    matcher = matcher.withMatcher(jpaExampleMatcher.filedName(), ExampleMatcher.GenericPropertyMatcher::contains); // 模糊匹配
                }
                if (jpaExampleMatcher.startsWith()) {
                    matcher = matcher.withMatcher(jpaExampleMatcher.filedName(), ExampleMatcher.GenericPropertyMatcher::startsWith); // 开头匹配
                }
                if (jpaExampleMatcher.endsWith()) {
                    matcher = matcher.withMatcher(jpaExampleMatcher.filedName(), ExampleMatcher.GenericPropertyMatcher::endsWith); // 末尾匹配
                }
                if (jpaExampleMatcher.eq()) {
                    matcher = matcher.withMatcher(jpaExampleMatcher.filedName(), ExampleMatcher.GenericPropertyMatcher::exact); // 精确匹配
                }
                if (jpaExampleMatcher.ignoreFiled()) {
                    matcher = matcher.withIgnorePaths(jpaExampleMatcher.filedName()); // 忽略哪些字段不作为匹配规则
                }
            }
        }
        return matcher;
    }

    /**
     * 返回最基本的Specification
     */
    public static <T> Specification<T> defaultSpec(Class<T> clazz) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    /**
     * 返回BaseCriteria对应拥有JPASpecification注解的非空字段的 fieldName:QueryCriteriaDTO 的键值对集合
     *
     * @param criteria 查询条件
     * @return 键值对集合
     * @throws IllegalAccessException 非法访问异常(此处为获取)
     */
    public static HashMap<String, QueryCriteriaDTO> opDTOFromCriteria(BaseCriteria criteria) throws IllegalAccessException {
        HashMap<String, QueryCriteriaDTO> result = new HashMap<>(16);
        Class<? extends BaseCriteria> criteriaClass = criteria.getClass();
        Field[] fields = criteriaClass.getDeclaredFields();
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(JPASpecification.class) && Objects.nonNull(field.get(criteria))) {
                JPASpecification jpaSpecification = field.getAnnotation(JPASpecification.class);
                Object value = field.get(criteria);
                QueryCriteriaDTO dto;
                // 如果是区间
                if (jpaSpecification.startOfSection()) {
                    dto = result.containsKey(jpaSpecification.filedName()) ? result.get(jpaSpecification.filedName()) : new QueryCriteriaDTO()
                            .setSingle(false)
                            .setFieldName(jpaSpecification.filedName())
                            .setStartValue(value.toString())
                            .setStartJpaSpecification(jpaSpecification);
                } else if (jpaSpecification.endOfSection()) {
                    dto = result.containsKey(jpaSpecification.filedName()) ? result.get(jpaSpecification.filedName()) : new QueryCriteriaDTO()
                            .setSingle(false)
                            .setFieldName(jpaSpecification.filedName())
                            .setEndValue(value.toString())
                            .setEndJpaSpecification(jpaSpecification);
                } else {
                    // 如果是单体
                    dto = new QueryCriteriaDTO()
                            .setSingle(true)
                            .setSingleJpaSpecification(jpaSpecification)
                            .setFieldName(jpaSpecification.filedName())
                            .setFieldValue(value.toString());
                }
                // 放入
                result.put(jpaSpecification.filedName(), dto);
            }
        }
        return result;
    }

    public static void putFieldToOpMap(HashMap<BaseCriteria.OPEnum, List<QueryCriteriaDTO>> opMap, QueryCriteriaDTO dto, BaseCriteria.OPEnum op) {
        List<QueryCriteriaDTO> list;
        if (opMap.containsKey(op)) {
            list = opMap.get(op);
        } else {
            list = new ArrayList<>();
        }
        list.add(dto);
        opMap.put(op, list);
    }

    /**
     * ①通过JPASpecificationFromCriteria()方法获取非空字段的->dbo的fieldName:JPASpecification注解的键值对集合<br/>
     * ②
     *
     * @param criteria      查询条件
     * @param criteriaClass BaseCriteria对应的子类的class
     * @return 键值对集合
     * @throws IllegalAccessException 非法访问异常(此处为获取)
     */
    public static <K> HashMap<BaseCriteria.OPEnum, List<QueryCriteriaDTO>> fieldsFromCriteria(BaseCriteria criteria, Class<K> criteriaClass) throws IllegalAccessException {
        // 操作集合
        HashMap<BaseCriteria.OPEnum, List<QueryCriteriaDTO>> opMap = new HashMap<>(8);
        //
        HashMap<String, QueryCriteriaDTO> fieldJPASpecMap = opDTOFromCriteria(criteria);
        for (String fieldName : fieldJPASpecMap.keySet()) {
            QueryCriteriaDTO queryCriteriaDTO = fieldJPASpecMap.get(fieldName);
            // 单体
            if (queryCriteriaDTO.getSingle()) {
                // 只能是EQ的,放入EQ操作中后，开始下一个字段
                if (queryCriteriaDTO.getSingleJpaSpecification().onlyEq()) {
                    putFieldToOpMap(opMap, queryCriteriaDTO, BaseCriteria.OPEnum.EQ);
                    continue;
                }
                // 其他操作：
                // 在请求DTO中该字段对应的查询条件字段名
                String queryCriteria = queryCriteriaDTO.getSingleJpaSpecification().queryCriteria();
                // 根据此字段名获得字段的值
                Field field = ReflectionUtils.findField(criteriaClass, queryCriteria);
                assert field != null;
                ReflectionUtils.makeAccessible(field);
                Object op = ReflectionUtils.getField(field, criteria);
                if (op instanceof BaseCriteria.OPEnum) {
                    putFieldToOpMap(opMap, queryCriteriaDTO, (BaseCriteria.OPEnum) op);
                }
            } else {
                // 区间BETWEEN操作
                if (Objects.nonNull(queryCriteriaDTO.getStartValue()) && Objects.nonNull(queryCriteriaDTO.getEndValue())) {
                    putFieldToOpMap(opMap, queryCriteriaDTO, BaseCriteria.OPEnum.BETWEEN);
                }
                // 其他非单体操作(如果有，在此处进行添加 ↓↓↓)

            }
        }
        return opMap;
    }


    public static <T, K> Specification<T> specFromCriteria(Class<T> clazz, BaseCriteria criteria, Class<K> criteriaClass) throws IllegalAccessException {
        HashMap<BaseCriteria.OPEnum, List<QueryCriteriaDTO>> opMap = fieldsFromCriteria(criteria, criteriaClass);
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            //
            List<Predicate> predicateList = new ArrayList<>();
            // 根据opMap组装Predicate然后返回
            for (BaseCriteria.OPEnum op : opMap.keySet()) {
                // 每一种操作组装成一个 Predicate
                predicateList.add(buildPredicate(opMap, op, root, query, criteriaBuilder));
            }
            if (CollUtil.isNotEmpty(predicateList)) {
                return criteriaBuilder.and(ArrayUtil.toArray(predicateList, Predicate.class));
            }
            return criteriaBuilder.conjunction();
        };
    }

    private static <T> Predicate buildPredicate(HashMap<BaseCriteria.OPEnum, List<QueryCriteriaDTO>> opMap, BaseCriteria.OPEnum op, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<QueryCriteriaDTO> dtoList = opMap.get(op);
        List<Predicate> predicateList = new ArrayList<>();
        for (QueryCriteriaDTO criteriaDTO : dtoList) {
            Predicate predicate;
            switch (op) {
                case EQ:
                    predicateList.add(criteriaBuilder.equal(root.get(criteriaDTO.getFieldName()), criteriaDTO.getFieldValue()));
                    break;
                case NEQ:
                    predicateList.add(criteriaBuilder.notEqual(root.get(criteriaDTO.getFieldName()), criteriaDTO.getFieldValue()));
                    break;
                case GT:
                    predicateList.add(criteriaBuilder.greaterThan(root.get(criteriaDTO.getFieldName()), criteriaDTO.getFieldValue()));
                    break;
                case GT_OR_EQ:
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get(criteriaDTO.getFieldName()), criteriaDTO.getFieldValue()));
                    break;
                case LT:
                    predicateList.add(criteriaBuilder.lessThan(root.get(criteriaDTO.getFieldName()), criteriaDTO.getFieldValue()));
                    break;
                case LT_OR_EQ:
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get(criteriaDTO.getFieldName()), criteriaDTO.getFieldValue()));
                    break;
                case START:
                    predicateList.add(criteriaBuilder.like(root.get(criteriaDTO.getFieldName()), criteriaDTO.getFieldValue() + "%"));
                    break;
                case END:
                    predicateList.add(criteriaBuilder.like(root.get(criteriaDTO.getFieldName()), "%" + criteriaDTO.getFieldValue()));
                    break;
                case CONTAINS:
                    predicateList.add(criteriaBuilder.like(root.get(criteriaDTO.getFieldName()), "%" + criteriaDTO.getFieldValue() + "%"));
                    break;
                case BETWEEN:
                    predicateList.add(criteriaBuilder.between(root.get(criteriaDTO.getFieldName()), criteriaDTO.getStartValue(), criteriaDTO.getEndValue()));
                    break;
                default:
                    log.warn("暂不支持的操作类型" + op.description());
                    break;
            }
        }
        return criteriaBuilder.and(ArrayUtil.toArray(predicateList, Predicate.class));
    }


}
