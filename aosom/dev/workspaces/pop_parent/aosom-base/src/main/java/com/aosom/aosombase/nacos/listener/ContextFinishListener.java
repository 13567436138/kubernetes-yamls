package com.aosom.aosombase.nacos.listener;

import com.alibaba.nacos.spring.core.env.NacosPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContextFinishListener implements ApplicationListener<ContextRefreshedEvent> {





    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Environment environment= event.getApplicationContext().getEnvironment();
             if(environment instanceof ConfigurableEnvironment){
                     //获取properties,发送配置更新信息
                 MutablePropertySources  propertySources=       ((ConfigurableEnvironment) environment).getPropertySources();

                 for(PropertySource propertySource : propertySources){
                       if(propertySource instanceof NacosPropertySource){
                             //发送跟新
                           NacosPropertySource  properties=(NacosPropertySource)    propertySource;
                           publishEvent(event.getApplicationContext(),properties);
                       }
                 }
             }
    }

    private  void publishEvent(ApplicationContext  context,NacosPropertySource  nacosPropertySource){
        FirstInitMoreSiteDataEvent  event=new FirstInitMoreSiteDataEvent(this,nacosPropertySource.getDataId(),
                nacosPropertySource.getGroupId(),nacosPropertySource.getType(),nacosPropertySource.getSource());
        context.publishEvent(event);
    }



    public  static  class  FirstInitMoreSiteDataEvent  extends ApplicationEvent {

        private final String dataId;

        private final String groupId;

        private final String type;

        private Map<String, Object>   sourceData;

        public FirstInitMoreSiteDataEvent(Object source, String dataId, String groupId, String type,Map<String, Object>  sourceData) {
            super(source);
            this.dataId=dataId;
            this.groupId=groupId;
             this.sourceData=sourceData;
            this.type = type;
        }

        /**
         * Get Content of published Nacos Configuration
         *
         * @return content
         */


        public String getType() {
            return type;
        }

        public String getDataId() {
            return dataId;
        }

        public String getGroupId() {
            return groupId;
        }

        public Map<String, Object> getSourceData() {
            return sourceData;
        }


    }


}
