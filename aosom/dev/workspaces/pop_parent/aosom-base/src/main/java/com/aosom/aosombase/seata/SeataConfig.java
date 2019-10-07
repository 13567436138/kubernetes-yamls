package com.aosom.aosombase.seata;

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 *     seata  使用  用  @GlobalTransactional  开启事务
 *     DataSourceProxy  代理数据源
 *     用数据库的方式   在数据库中执行 undo_log  建表语句
 */
@Configuration
public class SeataConfig {

    public static final String SEATA_XID = "Fescar_XID";

    @Bean
    public GlobalTransactionScanner globalTransactionScanner(Environment environment) {
        String applicationName = environment.getProperty("spring.application.name");
        String groupName = environment.getProperty("spring.cloud.alibaba.seata.tx-service-group");
        if (applicationName == null) {
            return new GlobalTransactionScanner(groupName == null ? "my_test_tx_group" : groupName);
        } else {
            return new GlobalTransactionScanner(applicationName, groupName == null ? "my_test_tx_group" : groupName);
        }
    }
}
