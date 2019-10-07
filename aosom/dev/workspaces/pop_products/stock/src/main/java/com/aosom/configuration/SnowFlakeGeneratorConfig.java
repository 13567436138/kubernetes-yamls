package com.aosom.configuration;

import com.aosom.aosombase.shardingsphere.AosomSnowFlakeShardingKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/26 10:02
 * @Description:
 */
@Configuration
public class SnowFlakeGeneratorConfig {

    @Bean
    public AosomSnowFlakeShardingKeyGenerator generator(){
        return new AosomSnowFlakeShardingKeyGenerator();
    }
}
