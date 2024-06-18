package cn.mnay.common.jpa.comment.service;


import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mnay.common.jpa.comment.dto.ColumnCommentDTO;
import cn.mnay.common.jpa.comment.service.impl.MysqlAlterCommentServiceImpl;
import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import cn.mnay.common.jpa.comment.annotation.TableComment;
import cn.mnay.common.jpa.comment.dto.TableCommentDTO;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.metamodel.model.domain.internal.SingularAttributeImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.*;


@Slf4j
public class JpaCommentService {

    @Setter
    AlterCommentService alterCommentService;
    Map<String, TableCommentDTO> dtoMap;
    Map<String, List<ColumnCommentDTO>> foreignKeyMap;
    @Setter
    private EntityManager entityManager;

    /**
     * 初始化JpaCommentService
     */
    public void init() {
        this.alterAllTableAndColumn();
        log.info("JpaCommentService 初始化成功...");
    }

    /**
     * 对所有表和字段都解析
     */
    public void alterAllTableAndColumn() {
        if (MapUtil.isEmpty(this.dtoMap)) {
            this.foreignKeyMap = MapUtil.newHashMap();
            this.dtoMap = this.findAllTableAndColumn();
        }

        this.dtoMap.forEach((key, value) -> {
            try {
                this.alterSingleTableAndColumn(key);
            } catch (Exception e) {
                log.error("tableName '{}' ALTER comment exception ", key, e);
            }

        });
    }

    public void alterSingleTableAndColumn(String tableName) {
        TableCommentDTO commentDTO = this.dtoMap.get(tableName);
        if (commentDTO != null) {
            if (StrUtil.isNotBlank(commentDTO.getComment())) {
                if (log.isDebugEnabled()) {
                    log.debug("修改表 {} 的注释为 '{}'", commentDTO.getName(), commentDTO.getComment());
                }

                try {
                    this.alterCommentService.alterTableComment(commentDTO.getSchema(), commentDTO.getName(), commentDTO.getComment());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("修改表 {} 的注释为 '{}' 失败，原因：{}", commentDTO.getName(), commentDTO.getComment(), e.getMessage());
                }
            }

            commentDTO.getColumnCommentDTOList().forEach((item) -> {
                if (StrUtil.isNotBlank(item.getComment())) {
                    if (log.isDebugEnabled()) {
                        log.debug("修改表 {} 字段 {} 的注释为 '{}'", commentDTO.getName(), item.getName(), item.getComment());
                    }

                    try {
                        if (item.isImportant() && this.alterCommentService instanceof MysqlAlterCommentServiceImpl) {
                            this.alterCommentService.getJdbcTemplate().execute("SET FOREIGN_KEY_CHECKS = 0;");
                            this.alterCommentService.alterColumnComment(commentDTO.getSchema(), commentDTO.getName(), item.getName(), item.getComment());
                            this.alterCommentService.getJdbcTemplate().execute("SET FOREIGN_KEY_CHECKS = 1;");
                        } else {
                            this.alterCommentService.alterColumnComment(commentDTO.getSchema(), commentDTO.getName(), item.getName(), item.getComment());
                        }
                    } catch (Exception e) {
                        log.error("修改表 {} 字段 {} 的注释为 '{}' 失败，原因：{}", commentDTO.getName(), item.getName(), item.getComment(), e.getMessage());
                    }
                }

            });
        } else {
            log.warn("tableName '{}' not find in JPA ", tableName);
        }

    }

    @SuppressWarnings("unchecked")
    public Map<String, TableCommentDTO> findAllTableAndColumn() {
        Map<String, TableCommentDTO> tableCommentMap = MapUtil.newHashMap();
        // 获取entityManager
        EntityManagerFactory entityManagerFactory = this.entityManager.getEntityManagerFactory();
        Set<EntityType<?>> entities = entityManagerFactory.getMetamodel().getEntities();
        entities.forEach(entity -> {
            TableCommentDTO table = new TableCommentDTO();
            String tableName = entity.getName();
            Class<?> targetClass = entity.getJavaType();
            Set<Attribute<?, ?>> attributes = (Set<Attribute<?, ?>>) entity.getAttributes();
            this.getTableInfoZzx(tableName, table, targetClass);
            this.getColumnInfoZzx(attributes, table, targetClass);
            this.getKeyColumnInfoZzx(attributes, table, targetClass);
            tableCommentMap.put(table.getName(), table);
        });
        this.getForeignKey(tableCommentMap);
        return tableCommentMap;
    }

    private void getForeignKey(Map<String, TableCommentDTO> tableCommentMap) {
        if (MapUtil.isNotEmpty(this.foreignKeyMap) && MapUtil.isNotEmpty(tableCommentMap)) {
            this.foreignKeyMap.forEach((key, value) -> {
                if (tableCommentMap.containsKey(key)) {
                    tableCommentMap.get(key).getColumnCommentDTOList().addAll(value);
                }
            });
        }

    }

    private void getTableInfoZzx(String tableName, TableCommentDTO table, Class<?> targetClass) {
        table.setColumnCommentDTOList(new ArrayList<>());
        table.setName(this.nameCast(tableName));
        TableComment tableComment = AnnotationUtil.getAnnotation(targetClass, TableComment.class);
        if (tableComment != null) {
            Table tableInfo = AnnotationUtil.getAnnotation(targetClass, Table.class);
            if (tableInfo != null) {
                String schema = StrUtil.toUnderlineCase(tableInfo.schema());
                // 如果手动指定了表的名称，以手动指定的表的名称为准
                if (StrUtil.isNotBlank(tableInfo.name())) {
                    table.setName(tableInfo.name());
                }
                table.setSchema(schema);
                log.debug("当前数据库schema为 {}", schema);
                table.setComment(tableComment.value());
            }
        } else {
            table.setComment("");
        }
    }

    /**
     * 转换为小写+下划线的字符串
     */
    private String nameCast(String name) {
        return StrUtil.toUnderlineCase(name);
    }


    /**
     * ①如果targetClass拥有MappedSuperclass注解，则加入到list中
     * ②如果targetClass有父类，继续去找父类，并重复①②动作，若没有父类则结束
     */
    private void getAllClass(Class<?> targetClass, List<Class<?>> list) {
        if (AnnotationUtil.hasAnnotation(targetClass, MappedSuperclass.class)) {
            list.add(targetClass);
        }

        if (!Object.class.equals(targetClass.getSuperclass())) {
            this.getAllClass(targetClass.getSuperclass(), list);
        }

    }

    private void getColumnInfoZzx(Set<Attribute<?, ?>> attributes, TableCommentDTO table, Class<?> targetClass) {
        List<Class<?>> classList = new ArrayList<>();
        classList.add(targetClass);
        this.getAllClass(targetClass, classList);
        Set<String> alreadyDealField = CollUtil.newHashSet();
        Set<String> allColumnField = CollUtil.newHashSet();
        Map<String, Attribute<?, ?>> allColumnFieldMap = MapUtil.newHashMap();

        attributes.forEach(attribute -> {
            allColumnField.add(attribute.getName());
            allColumnFieldMap.put(attribute.getName(), attribute);
        });

        classList.forEach((classItem) -> Arrays.stream(ClassUtil.getDeclaredFields(classItem)).forEach((field) -> {
            if (allColumnFieldMap.containsKey(field.getName()) && !alreadyDealField.contains(field.getName())) {
                // 如果该字段被ManyToOne标注，则执行对外键增加注释
                if (allColumnFieldMap.get(field.getName()).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.MANY_TO_ONE)) {
                    Attribute<?, ?> attribute = allColumnFieldMap.get(field.getName());
                    Member javaMember = attribute.getJavaMember();
                    Class<?> foreignClass = ((Field) javaMember).getType();
                    String id = null;
                    while (ObjectUtil.isNotNull(foreignClass)) {
                        Field[] declaredFields = ClassUtil.getDeclaredFields(foreignClass);
                        List<Field> collect = Arrays.stream(declaredFields)
                                .filter(foreignField -> Arrays.stream(foreignField.getAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(Id.class)))
                                .toList();
                        if (collect.isEmpty()) {
                            // 如果没有@ID注解则向父类查询
                            foreignClass = foreignClass.getSuperclass();
                        } else {
                            id = collect.getFirst().getName();
                            break;
                        }
                    }
                    String tableName = table.getName();
                    if (StrUtil.isEmpty(id)) {
                        throw new RuntimeException("表：" + tableName + "没有@Id标注的属性");
                    }
                    // 组装外键
                    String[] keyColumnNames = new String[]{javaMember.getName() + "_" + id};
                    this.getColumnComment(this.foreignKeyMap, classItem, tableName, field.getName(), keyColumnNames);
                } else {
                    String[] columnName = new String[]{this.nameCast(field.getName())};
                    this.getColumnComment(table, classItem, field.getName(), columnName);
                    alreadyDealField.add(field.getName());
                }
            }

        }));
    }

    private void getColumnComment(TableCommentDTO table, Class<?> targetClass, String propertyName, String[] columnName) {
        ColumnComment idColumnComment = AnnotationUtil.getAnnotation(ClassUtil.getDeclaredField(targetClass, propertyName), ColumnComment.class);
        Arrays.stream(columnName).forEach((item) -> {
            ColumnCommentDTO column = new ColumnCommentDTO();
            column.setName(item);
            if (idColumnComment != null) {
                column.setComment(idColumnComment.value());
                column.setImportant(true);
            } else {
                column.setComment("");
            }

            table.getColumnCommentDTOList().add(column);
        });
    }

    private void getKeyColumnInfoZzx(Set<Attribute<?, ?>> attributes, TableCommentDTO table, Class<?> targetClass) {
        Attribute<?, ?> id = attributes.stream().filter(attribute -> {
                    try {
                        // 除了ID，其他属性可能会报错
                        return ((SingularAttributeImpl.Identifier<?, ?>) attribute).isId();
                    } catch (Exception e) {
                        return false;
                    }
                }).findFirst()
                .orElseThrow(() -> new RuntimeException(table.getName() + " 没有ID属性"));
        String idName = id.getName();
        String[] idColumns;
        // 从自己和父类中获取id
        for (idColumns = new String[]{idName}; !Object.class.equals(targetClass); targetClass = targetClass.getSuperclass()) {
            ColumnComment idColumnComment = AnnotationUtil.getAnnotation(ClassUtil.getDeclaredField(targetClass, idName), ColumnComment.class);
            if (idColumnComment != null) {
                break;
            }
        }

        this.getColumnComment(table, targetClass, idName, idColumns);
    }

    private void getColumnComment(Map<String, List<ColumnCommentDTO>> foreignKeyMap, Class<?> targetClass, String tableName, String propertyName, String[] columnName) {
        ColumnComment idColumnComment = AnnotationUtil.getAnnotation(ClassUtil.getDeclaredField(targetClass, propertyName), ColumnComment.class);
        Arrays.stream(columnName).forEach((item) -> {
            ColumnCommentDTO column = new ColumnCommentDTO();
            column.setName(item);
            if (idColumnComment != null) {
                column.setComment(idColumnComment.value());
            } else {
                column.setComment("");
            }

            if (foreignKeyMap.containsKey(tableName)) {
                foreignKeyMap.get(tableName).add(column);
            } else {
                foreignKeyMap.put(tableName, ListUtil.toList(column));
            }

        });
    }

    public void setCurrentJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.alterCommentService.setJdbcTemplate(jdbcTemplate);
    }

}
