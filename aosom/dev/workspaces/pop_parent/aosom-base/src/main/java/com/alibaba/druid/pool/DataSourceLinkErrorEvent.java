package com.alibaba.druid.pool;

import org.springframework.context.ApplicationEvent;

public class DataSourceLinkErrorEvent  extends ApplicationEvent {


    private  String   dataSourceName;

    private  String   groupId;

    private  String  dataId;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DataSourceLinkErrorEvent(Object source,String dataSrouceName,String groupId,String dataId) {
        super(source);
        this.dataSourceName=dataSrouceName;
        this.groupId=groupId;
        this.dataId=dataId;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
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
