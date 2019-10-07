package com.aosom.aosombase.shardingsphere;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.nacos.spring.core.env.NacosPropertySource;
import com.google.common.base.Preconditions;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.shardingsphere.core.util.InlineExpressionParser;
import org.apache.shardingsphere.core.yaml.swapper.impl.EncryptRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.core.yaml.swapper.impl.MasterSlaveRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.core.yaml.swapper.impl.ShardingRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.shardingjdbc.api.EncryptDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.spring.boot.common.SpringBootPropertiesConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.encrypt.EncryptRuleCondition;
import org.apache.shardingsphere.shardingjdbc.spring.boot.encrypt.SpringBootEncryptRuleConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.masterslave.MasterSlaveRuleCondition;
import org.apache.shardingsphere.shardingjdbc.spring.boot.masterslave.SpringBootMasterSlaveRuleConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.ShardingRuleCondition;
import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.util.DataSourceUtil;
import org.apache.shardingsphere.shardingjdbc.spring.boot.util.PropertyUtil;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.*;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.beans.ConstructorProperties;
import java.sql.SQLException;
import java.util.*;

import static com.aosom.aosombase.nacos.MoreSiteContainer.datasource_group_id;


/**
 * 当前启动器会启动datasource组的数据
 * 当前对象替换了shardingsphere   的启动类
 */
@Configuration
@EnableConfigurationProperties({SpringBootShardingRuleConfigurationProperties.class, SpringBootMasterSlaveRuleConfigurationProperties.class, SpringBootEncryptRuleConfigurationProperties.class, SpringBootPropertiesConfigurationProperties.class})
@ConditionalOnProperty(
        prefix = "spring.shardingsphere",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
public class AosomShardingPhereConfiguration implements EnvironmentAware {

    private final SpringBootShardingRuleConfigurationProperties shardingRule;
    private final SpringBootMasterSlaveRuleConfigurationProperties masterSlaveRule;
    private final SpringBootEncryptRuleConfigurationProperties encryptRule;
    private final SpringBootPropertiesConfigurationProperties props;
    private final Map<String, DataSource> dataSourceMap = new LinkedHashMap();
    private final String jndiName = "jndi-name";

    @Bean
    @Conditional({ShardingRuleCondition.class})
    public DataSource shardingDataSource() throws SQLException {
        return ShardingDataSourceFactory.createDataSource(this.dataSourceMap, (new ShardingRuleConfigurationYamlSwapper()).swap(this.shardingRule), this.props.getProps());
    }

    @Bean
    @Conditional({MasterSlaveRuleCondition.class})
    public DataSource masterSlaveDataSource() throws SQLException {
        return MasterSlaveDataSourceFactory.createDataSource(this.dataSourceMap, (new MasterSlaveRuleConfigurationYamlSwapper()).swap(this.masterSlaveRule), this.props.getProps());
    }

    @Bean
    @Conditional({EncryptRuleCondition.class})
    public DataSource encryptDataSource() throws SQLException {
        return EncryptDataSourceFactory.createDataSource((DataSource)this.dataSourceMap.values().iterator().next(), (new EncryptRuleConfigurationYamlSwapper()).swap(this.encryptRule), this.props.getProps());
    }

    public final void setEnvironment(Environment environment) {
        MutablePropertySources  propertySources=((ConfigurableEnvironment) environment).getPropertySources();
        String prefix = "spring.shardingsphere.datasource.";
       // Iterator var3 = this.getDataSourceNames(environment, prefix).iterator();

//        while(var3.hasNext()) {
//            String each = (String)var3.next();
//
//            try {
//                this.dataSourceMap.put(each, this.getDataSource(environment, prefix, each));
//            } catch (ReflectiveOperationException var6) {
//                throw new ShardingException("Can't find datasource type!", var6);
//            } catch (NamingException var7) {
//                throw new ShardingException("Can't find JNDI datasource!", var7);
//            }
//        }

      //  putDataSource();
       //配置中心代码测试

        for(PropertySource propertySource : propertySources){
            if(propertySource instanceof NacosPropertySource){
                //发送跟新
                NacosPropertySource  properties=(NacosPropertySource)    propertySource;
                if(properties.getGroupId().equals(datasource_group_id)){
                    DataSourceParse. parseNacosProperty(properties,   this.dataSourceMap,this.masterSlaveRule);
                    break;
                }
            }
        }

      //  putDataSource();

    }



    private void putDataSource(){
//        spring.shardingsphere.datasource.usmaster.type=com.alibaba.druid.pool.MhDruidDataSource
//        spring.shardingsphere.datasource.usmaster.driver-class-name=com.mysql.cj.jdbc.Driver
//        spring.shardingsphere.datasource.usmaster.url=jdbc:mysql://database-829-instance-1.cdctfnonkvja.us-east-1.rds.amazonaws.com:3306/druidtest?serverTimezone=GMT%2B8
//        spring.shardingsphere.datasource.usmaster.username=admin
//        spring.shardingsphere.datasource.usmaster.password=aosom2019
//        spring.shardingsphere.datasource.usmaster.node=us

        DruidDataSource dataSource1=new DruidDataSource();
        dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://localhost:3306/test_a?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8");
        dataSource1.setUsername("root");
        dataSource1.setPassword("root");

        this.dataSourceMap.put("data1", new DataSourceProxy(dataSource1));
//        spring.shardingsphere.datasource.usslave1.type=com.alibaba.druid.pool.MhDruidDataSource
//        spring.shardingsphere.datasource.usslave1.driver-class-name=com.mysql.cj.jdbc.Driver
//        spring.shardingsphere.datasource.usslave1.url=jdbc:mysql://database-829-instance-1-us-east-1b.cdctfnonkvja.us-east-1.rds.amazonaws.com:3306/druidtest?serverTimezone=GMT%2B8
//        spring.shardingsphere.datasource.usslave1.username=admin
//        spring.shardingsphere.datasource.usslave1.password=aosom2019
//        spring.shardingsphere.datasource.usslave1.node=us

        DruidDataSource dataSource2=new DruidDataSource();
        dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource2.setUrl("jdbc:mysql://localhost:3306/test_b?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8");
        dataSource2.setUsername("root");
        dataSource2.setPassword("root");

        this.dataSourceMap.put("data3",  new DataSourceProxy(dataSource2));
//        spring.shardingsphere.datasource.euslave2.type=com.alibaba.druid.pool.MhDruidDataSource
//        spring.shardingsphere.datasource.euslave2.driver-class-name=com.mysql.cj.jdbc.Driver
//        spring.shardingsphere.datasource.euslave2.url=jdbc:mysql://database-829-eucopy.c3ruenfke0db.eu-central-1.rds.amazonaws.com:3306/druidtest?serverTimezone=GMT%2B8
//        spring.shardingsphere.datasource.euslave2.username=admin
//        spring.shardingsphere.datasource.euslave2.password=aosom2019
//        spring.shardingsphere.datasource.euslave2.node=eu
        DruidDataSource dataSource3=new DruidDataSource();
        dataSource3.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource3.setUrl("jdbc:mysql://localhost:3306/test_c?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8");
        dataSource3.setUsername("root");
        dataSource3.setPassword("root");
        this.dataSourceMap.put("data2",  new DataSourceProxy(dataSource3));
        initRule();
    }

    private  void initRule(){

        this.masterSlaveRule.setMasterDataSourceName("data1");
        this.masterSlaveRule.getSlaveDataSourceNames().add(  "data2");
        this.masterSlaveRule.getSlaveDataSourceNames().add(  "data3");
         //  this.encryptRule = encryptRule;
        Properties  properties=  this.props.getProps();
    }



    private List<String> getDataSourceNames(Environment environment, String prefix) {
        StandardEnvironment standardEnv = (StandardEnvironment)environment;
        standardEnv.setIgnoreUnresolvableNestedPlaceholders(true);
        return null == standardEnv.getProperty(prefix + "name") ? (new InlineExpressionParser(standardEnv.getProperty(prefix + "names"))).splitAndEvaluate() : Collections.singletonList(standardEnv.getProperty(prefix + "name"));
    }

    private DataSource getDataSource(Environment environment, String prefix, String dataSourceName) throws ReflectiveOperationException, NamingException {
        Map<String, Object> dataSourceProps = (Map) PropertyUtil.handle(environment, prefix + dataSourceName.trim(), Map.class);
        Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
        return dataSourceProps.containsKey("jndi-name") ? this.getJndiDataSource(dataSourceProps.get("jndi-name").toString()) : DataSourceUtil.getDataSource(dataSourceProps.get("type").toString(), dataSourceProps);
    }

    private DataSource getJndiDataSource(String jndiName) throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setResourceRef(true);
        bean.setJndiName(jndiName);
        bean.setProxyInterface(DataSource.class);
        bean.afterPropertiesSet();
        return (DataSource)bean.getObject();
    }

    @ConstructorProperties({"shardingRule", "masterSlaveRule", "encryptRule", "props"})
    public AosomShardingPhereConfiguration(SpringBootShardingRuleConfigurationProperties shardingRule, SpringBootMasterSlaveRuleConfigurationProperties masterSlaveRule, SpringBootEncryptRuleConfigurationProperties encryptRule, SpringBootPropertiesConfigurationProperties props) {
        this.shardingRule = shardingRule;
        this.masterSlaveRule = masterSlaveRule;
        this.encryptRule = encryptRule;
        this.props = props;
    }
}
