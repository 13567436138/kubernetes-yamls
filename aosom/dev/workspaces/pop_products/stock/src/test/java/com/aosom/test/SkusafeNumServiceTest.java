package com.aosom.test;

import com.aosom.aosombase.grpc.RpcSeataClientInterceptor;
import com.aosom.service.stock.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/24 19:13
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkusafeNumServiceTest {
    @GrpcClient(value = "stockService",interceptors = {RpcSeataClientInterceptor.class})
    private SkuSafeNumServiceGrpc.SkuSafeNumServiceBlockingStub  simpleBlockingStub;
    @Test
    public void testAdd(){
        SkuSafeNumRequest skuSafeNumRequest= SkuSafeNumRequest.newBuilder().setSafenum(100).setSiteid(100).setSkuid(6L).build();
        SkuSafeNumResponse response= simpleBlockingStub.add(skuSafeNumRequest);
        System.out.println(response.getIsSuccess());
    }

    @Test
    public void testDelete(){

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
        SkuSafeNumRequest skuSafeNumRequest=SkuSafeNumRequest.newBuilder().setSafenumStart(1)
                .setSafenumEnd(200).setSiteid(100).setCurrentPage(1).setPageSize(2).build();
        SkuSafeNumPageResponse response=simpleBlockingStub.findPage(skuSafeNumRequest);
        System.out.println(response.getCurrentPage());
        System.out.println(response.getTotal());
        System.out.println(response.getPageSize());
        System.out.println(response.getDataList());


    }
}
