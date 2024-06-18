package cn.mnay.common.jpa.comment.service.impl;


import cn.mnay.common.jpa.comment.service.AlterCommentService;
import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;


public class MysqlAlterCommentServiceImpl implements AlterCommentService {

    private String schema;
    private JdbcTemplate jdbcTemplate;
    String updateTableComment = "ALTER TABLE `%s`.`%s` COMMENT ?";
    String getUpdateColumnComment = "SELECT CONCAT('ALTER TABLE `', a.TABLE_SCHEMA, '`.`', a.TABLE_NAME, '` MODIFY COLUMN `', a.COLUMN_NAME, '` ', a.COLUMN_TYPE, (CASE WHEN a.IS_NULLABLE = 'NO' THEN ' NOT NULL ' ELSE '' END), (CASE WHEN a.COLUMN_DEFAULT IS NOT NULL THEN CONCAT(' DEFAULT ''', a.COLUMN_DEFAULT, ''' ') ELSE '' END), ' COMMENT ?') ALTER_SQL FROM information_schema.`COLUMNS` a WHERE a.TABLE_SCHEMA = ? AND a.TABLE_NAME = ? AND a.COLUMN_NAME = ?";
    private static final String TAG = "`";


    @Override
    public String getSchema() {
        return this.schema;
    }

    @Override
    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public void alterTableComment(String schema, String tableName, String tableComment) {
        if (tableName.contains(TAG)) {
            tableName = tableName.replace(TAG, "");
        }

        this.jdbcTemplate.update(String.format(this.updateTableComment, schema, tableName), tableComment);
    }

    @Override
    public void alterColumnComment(String schema, String tableName, String columnName, String columnComment) {
        if (tableName.contains(TAG)) {
            tableName = tableName.replace(TAG, "");
        }

        if (columnName.contains(TAG)) {
            columnName = columnName.replace(TAG, "");
        }

        String updateColumnComment = this.jdbcTemplate.queryForObject(this.getUpdateColumnComment, String.class, schema, tableName, columnName);
        if (StringUtils.isNotBlank((updateColumnComment))) {
            this.jdbcTemplate.update(updateColumnComment, columnComment);
        }
    }

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }
}
