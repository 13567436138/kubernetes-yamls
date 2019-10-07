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
 * @Date: 2019/9/24 19:12
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerSiteServiceTest {
    @GrpcClient(value = "stockService",interceptors = {RpcSeataClientInterceptor.class})
    private SellerSiteServiceGrpc.SellerSiteServiceBlockingStub  simpleBlockingStub;

    @Test
    public void testAdd(){
        SellerSiteRequest sellerSiteStockRequest= SellerSiteRequest.newBuilder()
                .setMode(1).setSiteid(100).setSellerid(1)
                .build();
        SellerSiteResponse response= simpleBlockingStub.add(sellerSiteStockRequest);
        System.out.println(response.getIsSuccess());
    }
}
