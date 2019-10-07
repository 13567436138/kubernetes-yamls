package com.aosom.aosombase.nacos.adapter;

import com.aosom.aosombase.nacos.MoreSiteContainer;
import com.aosom.aosombase.nacos.entity.MetaDataParseRule;
import com.aosom.aosombase.nacos.entity.MoreSiteMap;

import org.springframework.beans.TypeConverter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 解析获得到的数据，刷新到缓存中的对象
 */
public class PropertiesParseAndRefreshAdapter {


      public  static  void parseAndRefresh(MetaDataParseRule rule, Properties configProperties, String groupId, String  dataId , TypeConverter typeConverter){

          Set<String>   alreadyParsePropertyKeys=new HashSet<>();

          MoreSiteMap  moreSiteMap= MoreSiteContainer.getMoreSiteMap(groupId,dataId);
          for (Object key : configProperties.keySet()) {
              String propertyKey = (String)key;
              if(!alreadyParsePropertyKeys.contains(propertyKey)){
                  int index= propertyKey.indexOf(".");
                  if(index==-1){
                      //如果只有一个，说明没有站点，这个值暂存普通缓存
                  }else{
                      //解析
                      String  siteName=propertyKey.substring(0,index);
                      Object  siteConfig=   moreSiteMap.get(siteName);
                      if(siteConfig==null){
                          //如果为空，创建这个站点配置信息
                          crateSiteConfigByRule(moreSiteMap,siteName,alreadyParsePropertyKeys,configProperties,rule,typeConverter);
                      }else{
                          //如果已经有了，那么更新里面的配置
                          updateSiteConfig(moreSiteMap,siteName,alreadyParsePropertyKeys,configProperties,rule,typeConverter,siteConfig);
                      }
                  }
              }
          }
      }


    public  static  void parseAndRefresh(MetaDataParseRule  rule, Map<String,Object> configProperties,String groupId,String  dataId ,TypeConverter typeConverter){

        Set<String>   alreadyParsePropertyKeys=new HashSet<>();

        MoreSiteMap  moreSiteMap= MoreSiteContainer.getMoreSiteMap(groupId,dataId);
        for (Object key : configProperties.keySet()) {
            String propertyKey = (String)key;
            if(!alreadyParsePropertyKeys.contains(propertyKey)){
                int index= propertyKey.indexOf(".");
                if(index==-1){
                    //如果只有一个，说明没有站点，这个值暂存普通缓存
                }else{
                    //解析
                    String  siteName=propertyKey.substring(0,index);
                    Object  siteConfig=   moreSiteMap.get(siteName);
                    if(siteConfig==null){
                        //如果为空，创建这个站点配置信息
                        crateSiteConfigByRule(moreSiteMap,siteName,alreadyParsePropertyKeys,configProperties,rule,typeConverter);
                    }else{
                        //如果已经有了，那么更新里面的配置
                        updateSiteConfig(moreSiteMap,siteName,alreadyParsePropertyKeys,configProperties,rule,typeConverter,siteConfig);
                    }
                }
            }
        }
    }

    /**
     * 根据规则创建站点信息，并保存在站点信息列表中
     * @param moreSiteMap
     * @param siteName
     * @param alreadyParsePropertyKeys
     * @param configProperties
     * @param rule
     */
      private static   void   crateSiteConfigByRule(MoreSiteMap moreSiteMap, String siteName, Set<String> alreadyParsePropertyKeys , Properties configProperties, MetaDataParseRule  rule, TypeConverter typeConverter){

          HashMap<String, Field>  rules=rule.getRules();
          Set<String>  keys=rules .keySet();
          try {
              Object  instance= rule.getRuleClass().newInstance();
              for(String key:  keys){
                   Field field=rules.get(key);
                   String  propertyValue= configProperties.getProperty(siteName+"."+key);
                   alreadyParsePropertyKeys.add(siteName+"."+key);
                   ReflectionUtils.makeAccessible(field);
                   setField(field,propertyValue,instance,typeConverter);
              }
              moreSiteMap.put(siteName,instance);
          } catch (InstantiationException e) {
              e.printStackTrace();
          } catch (IllegalAccessException e) {
              e.printStackTrace();
          }

      }


    /**
     * 根据规则创建站点信息，并保存在站点信息列表中
     * @param moreSiteMap
     * @param siteName
     * @param alreadyParsePropertyKeys
     * @param configProperties
     * @param rule
     */
    private static   void   crateSiteConfigByRule( MoreSiteMap  moreSiteMap,String siteName,Set<String> alreadyParsePropertyKeys , Map<String,Object> configProperties,MetaDataParseRule  rule,TypeConverter typeConverter){

        HashMap<String, Field>  rules=rule.getRules();
        Set<String>  keys=rules .keySet();
        try {
            Object  instance= rule.getRuleClass().newInstance();
            for(String key:  keys){
                Field field=rules.get(key);
                String  propertyValue=(String) configProperties.get(siteName+"."+key);
                alreadyParsePropertyKeys.add(siteName+"."+key);
                ReflectionUtils.makeAccessible(field);
                setField(field,propertyValue,instance,typeConverter);
            }
            moreSiteMap.put(siteName,instance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置字段
     * @param field
     * @param propertyValue
     * @param bean
     * @param typeConverter
     */
     private  static void  setField( Field  field,  String propertyValue,Object  bean,TypeConverter  typeConverter) {

        String fieldName = field.getName();

        try {
            ReflectionUtils.makeAccessible(field);

            field.set(bean,   typeConverter.convertIfNecessary(propertyValue, field.getType(), field));


        } catch (Throwable e) {

        }
    }


    private  static void  updateSiteConfig(MoreSiteMap  moreSiteMap,String siteName,Set<String> alreadyParsePropertyKeys , Properties configProperties,MetaDataParseRule  rule,TypeConverter typeConverter ,Object instance){
           HashMap<String, Field>  rules=rule.getRules();
           Set<String>  keys=rules .keySet();
           for(String key:  keys){
               Field field=rules.get(key);
               String  propertyValue= configProperties.getProperty(siteName+"."+key);
               alreadyParsePropertyKeys.add(siteName+"."+key);
                field.setAccessible(true);
               setField(field,propertyValue,instance,typeConverter);
           }
           moreSiteMap.put(siteName,instance);
       }


    private  static void  updateSiteConfig(MoreSiteMap  moreSiteMap,String siteName,Set<String> alreadyParsePropertyKeys , Map<String,Object> configProperties,MetaDataParseRule  rule,TypeConverter typeConverter ,Object instance){
        HashMap<String, Field>  rules=rule.getRules();
        Set<String>  keys=rules .keySet();
        for(String key:  keys){
            Field field=rules.get(key);
            String  propertyValue= (String) configProperties.get(siteName+"."+key);
            alreadyParsePropertyKeys.add(siteName+"."+key);
            field.setAccessible(true);
            setField(field,propertyValue,instance,typeConverter);
        }
        moreSiteMap.put(siteName,instance);
    }

}
