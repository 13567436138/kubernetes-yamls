package com.aosom.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/25 17:08
 * @Description:
 */
@Configuration
public class MultiDatasource {
    @Primary
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(@Qualifier("sellerstockdbDataSource") DataSource sellerstockdbDataSource,
             @Qualifier("skustockdbDataSource") DataSource skustockdbDataSource,
                                        @Qualifier("stocklogdbDataSource") DataSource stocklogdbDataSource                            ) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(sellerstockdbDataSource);
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(DataSourceType.DataBaseType.sellerstockdb, sellerstockdbDataSource);
        dataSourceMap.put(DataSourceType.DataBaseType.skustockdb, skustockdbDataSource);
        dataSourceMap.put(DataSourceType.DataBaseType.stocklogdb, stocklogdbDataSource);
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }
}
