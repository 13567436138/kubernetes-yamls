package com.aosom.test;

import static org.junit.Assert.assertTrue;

import com.aosom.aosombase.grpc.RpcSeataClientInterceptor;
import com.aosom.service.stock.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * author:hxp
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTest
{
    @GrpcClient(value = "stockService",interceptors = {RpcSeataClientInterceptor.class})
    private StocksServiceGrpc.StocksServiceBlockingStub  simpleBlockingStub;

    @Test
    public void testDelete(){
        /*Stock.Builder  builder= Stock.newBuilder();
        builder.setId(1);
        StockResponse response= simpleBlockingStub.deleteByPrimaryKey(builder.build());
        System.out.println(response.getIsSuccess());*/
    }
    @Test
    public void testAdd(){
        /*StockRequest stockRequest= StockRequest.newBuilder()
                .setAddress("111").setCitycode("11").setCityname("宁波")
                .setCountry("中国").setCountrycode("CN")
                .setLat(11).setLng(22).setCountyname("香山")
                .setZipcode("315703").setCountycode("XS")
                .setStatename("浙江").setStatecode("ZJ")
                .setSellerid(1L).setPri(10).setName("浙江核心仓").build();
        StockResponse response= simpleBlockingStub.add(stockRequest);*/
        StockRequest stockRequest= StockRequest.newBuilder()
                .setAddress("111").setCitycode("nb").setCityname("宁波")
                .setCountry("中国").setCountrycode("CN")
                .setLat(4).setLng(24).setCountyname("金华")
                .setZipcode("315703").setCountycode("JH")
                .setStatename("浙江").setStatecode("ZJ")
                .setSellerid(1L).setPri(1).setName("浙江金华核心仓").build();
        StockResponse response= simpleBlockingStub.add(stockRequest);
        System.out.println(response.getIsSuccess());
    }
    @Test
    public void testUpdate(){

    }
    @Test
    public void testGet(){

    }
    @Test
    public void testFindAll(){

    }
    @Test
    public void testFindPage(){

    }
}
