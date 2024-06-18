package cn.mnay.common.jpa.comment.service;

import org.springframework.jdbc.core.JdbcTemplate;

public interface AlterCommentService {

    String getSchema();

    void setSchema(String schema);

    void alterTableComment(String schema, String tableName, String tableComment);

    void alterColumnComment(String schema, String tableName, String columnName, String columnComment);

    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    JdbcTemplate getJdbcTemplate();

}
