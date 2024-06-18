package cn.mnay.common.jpa.comment.service.impl;


import cn.mnay.common.jpa.comment.service.AlterCommentService;
import org.springframework.jdbc.core.JdbcTemplate;


public class SqlServerAlterCommentServiceImpl implements AlterCommentService {

    private String schema;
    private JdbcTemplate jdbcTemplate;
    String checkTableCommentExistsSql;
    String updateTableComment;
    String createTableComment;
    String checkColumnCommentExistsSql;
    String updateColumnComment;
    String createColumnComment;

    public SqlServerAlterCommentServiceImpl() {
        checkTableCommentExistsSql = "SELECT COUNT(*) FROM ::fn_listextendedproperty ('MS_Description','SCHEMA',?,'TABLE',?,NULL,NULL)";
        updateTableComment = " execute sp_updateextendedproperty 'MS_Description',?,'SCHEMA',?,'TABLE',?";
        createTableComment = " execute sp_addextendedproperty 'MS_Description',?,'SCHEMA',?,'TABLE',?";
        checkColumnCommentExistsSql = "SELECT COUNT(*) FROM ::fn_listextendedproperty ('MS_Description','SCHEMA',?,'TABLE',?,'COLUMN',?)";
        updateColumnComment = " execute sp_updateextendedproperty 'MS_Description',?,'SCHEMA',?,'TABLE',?,'COLUMN',? ";
        createColumnComment = " execute sp_addextendedproperty 'MS_Description',?,'SCHEMA',?,'TABLE',?,'COLUMN',? ";
    }


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
        Integer count = this.jdbcTemplate.queryForObject(this.checkTableCommentExistsSql, Integer.class, schema, tableName);
        if (count != null && !count.equals(0)) {
            this.jdbcTemplate.update(this.updateTableComment, tableComment, schema, tableName);
        } else {
            this.jdbcTemplate.update(this.createTableComment, tableComment, schema, tableName);
        }
    }

    @Override
    public void alterColumnComment(String schema, String tableName, String columnName, String columnComment) {
        Integer count = this.jdbcTemplate.queryForObject(this.checkColumnCommentExistsSql, Integer.class, this.schema, tableName, columnName);
        if (count != null && !count.equals(0)) {
            this.jdbcTemplate.update(this.updateColumnComment, columnComment, schema, tableName, columnName);
        } else {
            this.jdbcTemplate.update(this.createColumnComment, columnComment, schema, tableName, columnName);
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
