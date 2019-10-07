package com.aosom.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/25 16:38
 * @Description:
 */
@Configuration
@MapperScan(basePackages = "com.aosom.mapper.skustockdb", sqlSessionFactoryRef = "skustockdbSqlSessionFactory" , sqlSessionTemplateRef="skustockdbSqlSessionTemplate")
public class DataSourceConfigSkustockdb {
    @Bean(name = "skustockdbDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.skustockdb")
    public DataSource getDateSourceSellerstockdb(Environment environment) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(environment.getProperty("spring.datasource.skustockdb.jdbc-url"));
        try {
            dataSource.setDriver(DriverManager.getDriver(environment.getProperty("spring.datasource.skustockdb.jdbc-url")));
        } catch (SQLException e) {
            throw new RuntimeException("can not recognize datasource driver");
        }
        dataSource.setUsername(environment.getProperty("spring.datasource.skustockdb.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.skustockdb.password"));
        return new DataSourceProxy(dataSource);
    }
    @Bean(name = "skustockdbSqlSessionFactory")
    public SqlSessionFactory test1SqlSessionFactory(@Qualifier("skustockdbDataSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/skustockdb/*.xml"));
        return bean.getObject();
    }

    @Bean("skustockdbSqlSessionTemplate")
    public SqlSessionTemplate test1sqlsessiontemplate( @Qualifier("skustockdbSqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}
