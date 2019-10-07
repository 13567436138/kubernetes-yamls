package com.aosom.test;

import com.aosom.aosombase.grpc.RpcSeataClientInterceptor;
import com.aosom.service.stock.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/24 19:14
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkuStockInfoServiceTest {
    @GrpcClient(value = "stockService",interceptors = {RpcSeataClientInterceptor.class})
    private SkuStockInfoServiceGrpc.SkuStockInfoServiceBlockingStub  simpleBlockingStub;
    @Test
    public void testAdd(){
        SkuStockInfoRequest skuStockInfoRequest= SkuStockInfoRequest.newBuilder()
                .setLocknum(1).setRealnum(10).setSellerid(1)
                .setSkuid(6).setStockid(383927010004570113L)
                .build();
        SkuStockInfoResponse response= simpleBlockingStub.add(skuStockInfoRequest);
        System.out.println(response.getIsSuccess());
    }
    @Test
    public void testDelete(){

    }
    @Test
    public void testUpdate(){

    }
    @Test
    public void testFindPage(){

    }
}
