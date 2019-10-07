package com.aosom.aosombase.nacos.listener;

import com.aosom.aosombase.nacos.MoreSiteContainer;
import com.aosom.aosombase.nacos.entity.MoreSiteMap;
import com.aosom.aosombase.shardingsphere.AosomDataSource;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RefreshDataSourceListener  implements ApplicationListener<RefreshDataSourceListener.RefreshDataSourceEvent> {


    /**
     * 监听数据源是否要刷新
     * @param event
     */
    @Override
    public void onApplicationEvent(RefreshDataSourceEvent event) {


        MoreSiteMap<AosomDataSource> mapkey= MoreSiteContainer.getMoreSiteMap(event.getGroupId(),event.getDataId());


        //刷新数据源
          for(String key : mapkey.keySet() ){
                  mapkey.get(key).refresh();
          }
    }

    public  static  class  RefreshDataSourceEvent  extends ApplicationEvent{

          private  String groupId;

          private  String dataId;

        /**
         * Create a new ApplicationEvent.
         *
         * @param source the object on which the event initially occurred (never {@code null})
         */
        public RefreshDataSourceEvent(Object source,String groupId,String dataId) {
            super(source);
            this.groupId=groupId;
            this.dataId=dataId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getDataId() {
            return dataId;
        }

        public void setDataId(String dataId) {
            this.dataId = dataId;
        }
    }
}
