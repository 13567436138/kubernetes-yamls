package com.aosom.aosombase.nacos;




import com.aosom.aosombase.nacos.entity.BaseTool;
import com.aosom.aosombase.nacos.entity.MetaDataParseRule;
import com.aosom.aosombase.nacos.entity.MoreSiteMap;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * nacos  原生使用    拉去数据源 @NacosPropertySource( groupId = "datasource", dataId = "datasourceId", autoRefreshed = true)
 *                     获取的数据源直接放入内存  @NacosValue(value = "${site1.url:default}", autoRefreshed = true)
 *                     nacos.config.server-addr=127.0.0.1:8848
 *                     management.endpoints.web.exposure.include=*
 *                     management.endpoint.health.show-details=always
 *                     spring.cloud.nacos.config.file-extension=yml
 *
 *
 *      //多数据源的获取  解析模板配合如下
 *      @MoreSiteEntity(groupId = "test",dataId = "testjson")
            public class DataSourceSite {
            @MoreSiteValue("${url}")
            private  String url;
            @MoreSiteValue("${password}")
            private  String  passWord;
            @MoreSiteValue("${userName}")
            private   String userName;
            @MoreSiteValue("${id}")
            private  int id;
    }
 *
 * 数据对外
 * 可以用这个静态对象获取多数据
 */
public class MoreSiteContainer {

    private  static  int workId=-1;   //配置中心分配的workId


    public  static  final  String   datasource_group_id="datasource";   //数据库标识

    //解析模型的保存
    private  static   HashMap<String ,MetaDataParseRule> metaDataCacheRules=new HashMap<>();

    public static HashMap<String, MetaDataParseRule> getMetaDataCacheRules() {
        return metaDataCacheRules;
    }

    public static void setMetaDataCacheRules(HashMap<String, MetaDataParseRule> metaDataCacheRules) {
        MoreSiteContainer.metaDataCacheRules = metaDataCacheRules;
    }

    /**
     * 站点信息
     */
    private  static ConcurrentHashMap<String,MoreSiteMap>   siteDataCash=new ConcurrentHashMap<>();

    /**
     *根据groupId,dataId, site  获取数据  这边要检查类
     * @param <T>
     * @param groupId
     * @param dataId
     * @param site
     * @return
     */
       public  static  <T>  T  getMoreSiteData(String groupId ,String dataId,String site){

           MoreSiteMap   siteMap= siteDataCash.get(BaseTool.generageRuleCasheId(groupId,dataId));
           if(siteMap!=null){
                  Object  value=siteMap.get(site);;

                   return (T)value;
           }

           return  null;
       }

    /**
     * 获得当前groupId+dataId  的所有多站点信息
     * @return
     */
      public   static   MoreSiteMap  getMoreSiteMap(String groupId,String dataId){
             MoreSiteMap   moreSiteMap= siteDataCash.get(BaseTool.generageRuleCasheId(groupId,dataId));
             if(moreSiteMap==null){
                 moreSiteMap=new MoreSiteMap();
                 siteDataCash.put(BaseTool.generageRuleCasheId(groupId,dataId),moreSiteMap);
             }

           return moreSiteMap;
      }


    /**
     * 保存站点信息
     * @param groupId
     * @param dataId
     * @param site
     * @param data
     * @param <T>
     */
       public  static <T> void  putMoreSiteData(String groupId,String dataId,String site,T  data){
           String   casheId=BaseTool.generageRuleCasheId(groupId,dataId);
           MoreSiteMap   siteMap= siteDataCash.get(casheId);
          if(siteMap==null){
               siteMap=new MoreSiteMap<T>();
              siteDataCash.put(casheId,siteMap);
          }
           siteMap.put(site,data);
       }


    public static int getWorkId() {
        return workId;
    }

    public static void setWorkId(int workId) {
        MoreSiteContainer.workId = workId;
    }


}
