package cn.mnay.common.jpa.comment.config;



import cn.mnay.common.jpa.comment.service.AlterCommentService;
import cn.mnay.common.jpa.comment.service.JpaCommentService;
import cn.mnay.common.jpa.comment.service.impl.MysqlAlterCommentServiceImpl;
import cn.mnay.common.jpa.comment.service.impl.OracleAlterCommentServiceImpl;
import cn.mnay.common.jpa.comment.service.impl.PgSqlAlterCommentServiceImpl;
import cn.mnay.common.jpa.comment.service.impl.SqlServerAlterCommentServiceImpl;
import cn.mnay.common.jpa.comment.enums.DbTypeEnum;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author Tethamo_zzx
 * @date 2022-3-24  下午 06:01
 */
@Slf4j
@Configuration
@AutoConfigureAfter({EntityManager.class, JdbcTemplate.class, JpaProperties.class})
@ConditionalOnProperty(
        prefix = "jpa.comment",
        name = {"enable"},
        havingValue = "true"
)
public class JpaCommentAutoConfig {

    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    DataSource dataSource;
    @Resource
    JpaProperties jpaProperties;

    /**
     * 主要实现的是，当你的bean被注册之后，如果而注册相同类型的bean，就不会成功，它会保证你的bean只有一个，即你的实例只有一个
     * 当你注册多个相同的bean时，会出现异常，以此来告诉开发人员
     */
    @Bean
    @ConditionalOnMissingBean
    public AlterCommentService alterCommentService() throws SQLException {
        DatabaseMetaData metaData = this.dataSource.getConnection().getMetaData();
        String databaseType = metaData.getDatabaseProductName().toUpperCase();
        AlterCommentService service;
        if (databaseType.contains(DbTypeEnum.MYSQL.getValue())) {
            service = new MysqlAlterCommentServiceImpl();
        } else if (databaseType.contains(DbTypeEnum.SQLSERVER.getValue())) {
            service = new SqlServerAlterCommentServiceImpl();
        } else if (databaseType.contains(DbTypeEnum.ORACLE.getValue())) {
            service = new OracleAlterCommentServiceImpl();
        } else if (databaseType.contains(DbTypeEnum.POSTGRESQL.getValue())) {
            service = new PgSqlAlterCommentServiceImpl();
        } else {
            service = null;
            log.error("没有找到匹配的 DatabaseProductName {}", databaseType);
        }
        if (service != null) {
            service.setJdbcTemplate(this.jdbcTemplate);
        }
        return service;
    }


    /**
     * 该bean对象被创建出来以后，调用initMethod指定的方法去初始化
     */
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public JpaCommentService jpaCommentService() throws SQLException {
        JpaCommentService service = new JpaCommentService();
        service.setEntityManager(this.entityManager);
        service.setAlterCommentService(this.alterCommentService());
        service.setCurrentJdbcTemplate(this.jdbcTemplate);
        return service;
    }

}