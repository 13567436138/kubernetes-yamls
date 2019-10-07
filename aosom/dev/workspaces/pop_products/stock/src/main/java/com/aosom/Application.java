package com.aosom;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.aosom.aosombase.shardingsphere.AosomShardingPhereConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@MapperScan("com.aosom.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class Application
{

    public static void main( String[] args )
    {
        SpringApplication.run(Application.class,args);
    }
}
