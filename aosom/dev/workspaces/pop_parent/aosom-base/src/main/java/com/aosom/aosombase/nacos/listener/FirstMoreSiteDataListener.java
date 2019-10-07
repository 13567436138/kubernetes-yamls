package com.aosom.aosombase.nacos.listener;

import com.aosom.aosombase.nacos.MoreSiteContainer;
import com.aosom.aosombase.nacos.adapter.PropertiesParseAndRefreshAdapter;
import com.aosom.aosombase.nacos.entity.MetaDataParseRule;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.aosom.aosombase.nacos.entity.BaseTool.generageRuleCasheId;


@Component
public class FirstMoreSiteDataListener  implements ApplicationListener<ContextFinishListener.FirstInitMoreSiteDataEvent> {


    @Override
    public void onApplicationEvent(ContextFinishListener.FirstInitMoreSiteDataEvent event) {
        //判断我是否要接受数据
        String  metaCasheId=generageRuleCasheId(event.getGroupId(),event.getDataId());
        MetaDataParseRule rule=    MoreSiteContainer.getMetaDataCacheRules().get(metaCasheId);
        if(rule==null){
            return;
        }

        Map<String,Object> content = event.getSourceData();
        if (content != null) {
            String groupId=event.getGroupId();
            String dataId=event.getDataId();

          PropertiesParseAndRefreshAdapter.parseAndRefresh(rule,content,groupId,dataId,getTypeConverter());
        }
    }


    private TypeConverter converter =null;
    private TypeConverter getTypeConverter() {

        if(converter==null){
            converter=new SimpleTypeConverter();
        }
        return  converter;
    }
}
