package com.aosom.aosombase.shardingsphere;

import com.alibaba.nacos.spring.core.env.NacosPropertySource;

import com.aosom.aosombase.nacos.MoreSiteBeanFactoryPostProcessor;
import com.aosom.aosombase.nacos.MoreSiteContainer;
import com.aosom.aosombase.nacos.adapter.PropertiesParseAndRefreshAdapter;
import com.aosom.aosombase.nacos.entity.BaseTool;
import com.aosom.aosombase.nacos.entity.MetaDataParseRule;
import com.aosom.aosombase.nacos.entity.MoreSiteMap;
import org.apache.shardingsphere.shardingjdbc.spring.boot.masterslave.SpringBootMasterSlaveRuleConfigurationProperties;
import org.springframework.beans.SimpleTypeConverter;

import javax.sql.DataSource;
import java.util.Map;

public class DataSourceParse {


    /**
     * 解析获取到的数据
     * @param properties
     * @param dataSourceMap
     */
    public  static  void    parseNacosProperty(   NacosPropertySource properties ,final   Map<String, DataSource> dataSourceMap,final SpringBootMasterSlaveRuleConfigurationProperties masterSlaveRule){
             Map<String,Object> content=   properties.getSource();
             String dataId=  properties.getDataId();
             String groupId=properties.getGroupId();
             String ruleId= BaseTool.generageRuleCasheId(groupId,dataId);
             //存储模板
              MoreSiteBeanFactoryPostProcessor.parseClassForRule(ruleId,AosomDataSource.class.getName(),AosomDataSource.class);


              MetaDataParseRule rule=    MoreSiteContainer.getMetaDataCacheRules().get(ruleId);
              PropertiesParseAndRefreshAdapter.parseAndRefresh(rule,content,groupId,dataId,new SimpleTypeConverter());


              MoreSiteMap<AosomDataSource> moreSiteMap= MoreSiteContainer.getMoreSiteMap(groupId,dataId);

              for(String key : moreSiteMap.keySet()){
                  AosomDataSource dataSource=moreSiteMap.get(key);
                  dataSource.setName(key);  //设置数据库名称   如果不行，我们可以自行设置变量
                  dataSourceMap.put(key,dataSource.getDataSourceForDruid());
                  if(dataSource.getMaster()!=null&&dataSource.getMaster()){
                      masterSlaveRule.setMasterDataSourceName(key);
                  }else{
                      masterSlaveRule.getSlaveDataSourceNames().add(  key);
                  }
              }

        }
}
