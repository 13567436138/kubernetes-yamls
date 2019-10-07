package com.aosom.aosombase.shardingsphere;

import com.alibaba.druid.pool.DruidDataSource;

import com.aosom.aosombase.nacos.annotation.MoreSiteValue;
import io.seata.rm.datasource.DataSourceProxy;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 默认接受数据源
 */
public class AosomDataSource {

        private DataSource   dataSource=null;   //数据源
        private  String   name;//dataSourceName

        @MoreSiteValue("${username}")
        private  String  userName;

        @MoreSiteValue("${password}")
        private  String password;

        @MoreSiteValue("${url}")
        private  String  url;

        @MoreSiteValue("${driverClassName}")
        private  String driverClassName;

        @MoreSiteValue("${master}")
        private  Boolean  isMaster;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public Boolean getMaster() {
        return isMaster;
    }

    public void setMaster(Boolean master) {
        isMaster = master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得数据源
     * @return
     */
     public DataSource  getDataSourceForDruid(){
         if(dataSource==null) {
             DruidDataSource ds=new DruidDataSource();
             ds.setDriverClassName(this.getDriverClassName());
             ds.setUrl(this.getUrl());
             ds.setUsername(this.getUserName());
             ds.setPassword(this.getPassword());
             ds.setName(this.getName());
            this.dataSource=new DataSourceProxy(ds);
         }
         return dataSource;
     }


     public  void refresh(){
         if(dataSource!=null) {
              if(dataSource instanceof  DruidDataSource){
                  DruidDataSource temp=  ((DruidDataSource) dataSource);
                  refrehDruidDataSource(temp);

              }else if(dataSource instanceof  DataSourceProxy){
                  DataSource  ds=         ((DataSourceProxy)dataSource).getTargetDataSource();
                  if(ds instanceof  DruidDataSource){
                      DruidDataSource temp=  ((DruidDataSource) ds);
                      refrehDruidDataSource(temp);
                  }
              }
         }
     }

     public   void  refrehDruidDataSource( DruidDataSource temp){
         temp.setDriverClassName(this.getDriverClassName());
         temp.setUrl(this.getUrl());
         temp.setUsername(this.getUserName());
         temp.setPassword(this.getPassword());
         try {
             temp.restart();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }


}
