package com.aosom.aosombase.nacos.listener;

import com.alibaba.druid.pool.DataSourceLinkErrorEvent;
import com.alibaba.druid.pool.DruidDataSource;

import com.aosom.aosombase.nacos.MoreSiteContainer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 监听故障，数据具体要停止还是开放，有我来确定
 */

public class FaultAnalysisListener  implements ApplicationListener<DataSourceLinkErrorEvent>,ApplicationContextAware {



     public    static FaultRegister   faultRegister=new FaultRegister();  //错误记录器




    @Override
    public void onApplicationEvent(DataSourceLinkErrorEvent event) {

         String  dataSourceName = event.getDataSourceName();//数据库名称
         String groupId=event.getGroupId();
         String dataId=event.getDataId();
         faultRegister.registerFaultDataSource(dataSourceName,groupId,dataId);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

          faultRegister.setApplicationContext(applicationContext);   //设置Comtext  用于发送消息
    }


    public  static class  FaultRegister{

          private   static  final   long  TOLERENCE_ERROR=10L;//连接的容忍量，超过这个容忍量，就处理
          private   static  final   long  MIMITS_UNIT=10000L;//连接的容忍量，超过这个容忍量，就处理
          private  ApplicationContext  applicationContext;

          private Map<String,Integer> faultCount=new  HashMap<>();  //数据库错误的次数记录

          private  List<String> currentFaultList = new ArrayList<>();   //当前错误的数据库  释放的时间
         private   ExecutorService  executorService= null;

         //这边做
        public ApplicationContext getApplicationContext() {
            return applicationContext;
        }

        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }


        public void registerFaultDataSource(String name,String groupId , String  dataId){
                 //保存错误数量
                 int count=0;
                if(faultCount.containsKey(name)){
                    count=faultCount.get(name)+1;
                    faultCount.put(name,count )   ;
                }else{
                    count=1;
                    faultCount.put(name, 1);
                }
                //如果已经存在，那么直接退出
                if(currentFaultList.contains(name)){
                    return;
                }
                //提交启动任务
                if(TOLERENCE_ERROR<count){
                    //超过容忍
                    long temp= count/TOLERENCE_ERROR*MIMITS_UNIT;
                    currentFaultList.add(name);

                    StartDataSourceTask  task=new StartDataSourceTask(temp,name,groupId,dataId);
                    submitTask(task);

                    //关闭数据库连接池
                    DataSource  dataSource= MoreSiteContainer.getMoreSiteData(groupId,dataId,name);

                    if(dataSource!=null&&  dataSource instanceof DruidDataSource){
                        ((DruidDataSource)dataSource).close();  //关闭数据库连接池
                    }
                }

        }

        /**
         * 提交任务
         * @param runnable
         */
        private  synchronized   void   submitTask(Runnable  runnable){
            if(executorService==null){
                executorService=Executors.newCachedThreadPool();
            }

            executorService.submit(runnable);
        }
    }

    /**
     * 数据库启动任务
     */
    public  static  class   StartDataSourceTask implements Runnable{
        private   long sleepTime=0;

        private  String dataSourceName;

        private  String groupId;
        private  String dataId;



         public  StartDataSourceTask(long time,String name,String groupId,String dataId){
             sleepTime=time;
             this.dataSourceName=name;
             this.groupId=groupId;
             this.dataId=dataId;
         }

        @Override
        public void run(){
            try {
                Thread.sleep(sleepTime);

            DataSource  dataSource= MoreSiteContainer.getMoreSiteData(groupId,dataId,dataSourceName);

            if(dataSource!=null&&  dataSource instanceof DruidDataSource){
                ((DruidDataSource)dataSource).restart();
            }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }






}
