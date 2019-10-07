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
 * @Date: 2019/9/24 19:13
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerStockPolicyServiceTest {
    @GrpcClient(value = "stockService",interceptors = {RpcSeataClientInterceptor.class})
    private SellerStockPolicyServiceGrpc.SellerStockPolicyServiceBlockingStub  simpleBlockingStub;
    @Test
    public void testAdd(){
        SellerStockPolicyRequest sellerStockPolicyRequest= SellerStockPolicyRequest.newBuilder()
                .setCountrycode("CN").setStatecode("ZJ").setCountycode("XS")
                .setSellerid(1).setSiteid(100)
                .setStockidlist("[383927010004570113,1,2]").build();
        SellerStockPolicyResponse response= simpleBlockingStub.add(sellerStockPolicyRequest);
        System.out.println(response.getIsSuccess());
    }
    @Test
    public void testUpdate(){

    }
    @Test
    public void testGet(){

    }
    @Test
    public void testDelete(){

    }
    @Test
    public void testFindPage(){

    }
}
