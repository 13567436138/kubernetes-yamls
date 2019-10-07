package com.aosom.aosombase.nacos.workid;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.http.ServerHttpAgent;
import com.alibaba.nacos.client.config.impl.ClientWorker;
import com.alibaba.nacos.client.config.impl.HttpSimpleClient;
import com.alibaba.nacos.client.utils.LogUtils;
import com.aosom.aosombase.nacos.MoreSiteContainer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


/**
 * 当springboot  启动的时候
 */

public class WorkIdInitListener  implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LogUtils.logger(ClientWorker.class);
          @Value("${spring.cloud.nacos.config.server-addr}")
         private  String nacosAddress;
          @Value("${spring.application.name}")
          private  String   serverName;
           @Value("${server.port}")
          private String  port;
       List<String> paramValues=null;
       String  ip=null;

        private   ServerHttpAgent   agent;
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
                  //用nacos 方法
               ip=getLocalIp();

              paramValues= Arrays.asList("ip", ip, "port",port,"serverName",serverName);

            try {
                Properties properties=new Properties();
                properties.put(PropertyKeyConst.SERVER_ADDR,"127.0.0.1:8848" );
                agent=new ServerHttpAgent(properties);
                HttpSimpleClient.HttpResult  result=    agent.httpGet("/v1/cs/workid",null,paramValues,"UTF-8",5000);
                String workId=result.content;
                MoreSiteContainer.setWorkId(Integer.parseInt(workId.trim()));
                //设置work
                startHeart();
            } catch (NacosException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

       private    ScheduledExecutorService executorService;

        /**
         * 开始心跳
         */
         private  void startHeart(){
             paramValues=Arrays.asList("ip", ip, "port",port,"serverName",serverName,"workId",MoreSiteContainer.getWorkId()+"");

             executorService= Executors.newScheduledThreadPool(1, new ThreadFactory() {
                 @Override
                 public Thread newThread(Runnable r) {
                     Thread t = new Thread(r);
                     t.setName("get  work_id." + agent.getName());
                     t.setDaemon(true);
                     return t;
                 }
             });
             executorService.scheduleWithFixedDelay(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         //发送workid  心跳
                           sendWorkIdToNacos();
                     } catch (Throwable e) {
                         LOGGER.error("[" + agent.getName() + "] get  work_id   error", e);
                     }
                 }
             }, 1L, 15L, TimeUnit.SECONDS);
        }


        private void  sendWorkIdToNacos() throws IOException {

            HttpSimpleClient.HttpResult  result=    agent.httpPost("/v1/cs/workid",null,paramValues,"UTF-8",5000);
            String workId=result.content;

            MoreSiteContainer.setWorkId(Integer.parseInt(workId.trim()));
        }


        private  String getLocalIp()  {
            Enumeration<NetworkInterface> allNetInterfaces = null;
            try {
                allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                while (allNetInterfaces.hasMoreElements()){
                    NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()){
                        InetAddress ip = (InetAddress) addresses.nextElement();
                        if (ip != null
                                && ip instanceof Inet4Address
                                && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                                && ip.getHostAddress().indexOf(":")==-1){
                            System.out.println("本机的IP = " + ip.getHostAddress());
//                     return ip.getHostAddress();
                            return  ip.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }


            return "127.0.0.1";
        }









}
