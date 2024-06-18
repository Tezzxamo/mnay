package cn.mnay.common.jpa.comment.service.impl;


import cn.mnay.common.jpa.comment.service.AlterCommentService;
import org.springframework.jdbc.core.JdbcTemplate;


public class PgSqlAlterCommentServiceImpl implements AlterCommentService {

    private String schema;
    private JdbcTemplate jdbcTemplate;
    String updateTableComment = "COMMENT ON TABLE %s.%s IS '%s';";
    String updateColumnComment = "COMMENT ON COLUMN %s.%s.%s IS '%s'";


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
        if (tableName.contains("\"")) {
            tableName = tableName.replace("\"", "");
        }

        this.jdbcTemplate.update(String.format(this.updateTableComment, schema, tableName.toUpperCase(), tableComment));
    }

    @Override
    public void alterColumnComment(String schema, String tableName, String columnName, String columnComment) {
        if (tableName.contains("\"")) {
            tableName = tableName.replace("\"", "");
        }

        if (columnName.contains("\"")) {
            columnName = columnName.replace("\"", "");
        }

        this.jdbcTemplate.update(String.format(this.updateColumnComment, schema, tableName.toUpperCase(), columnName.toUpperCase(), columnComment));

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
