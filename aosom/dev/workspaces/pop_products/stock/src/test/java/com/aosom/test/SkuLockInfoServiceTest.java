package com.aosom.test;

import com.aosom.aosombase.grpc.RpcSeataClientInterceptor;
import com.aosom.aosombase.grpc.SellerContext;
import com.aosom.aosombase.seata.SeataConfig;
import com.aosom.service.SkuLockInfoServiceClientService;
import com.aosom.service.stock.*;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/24 19:13
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkuLockInfoServiceTest {
    @Autowired
    private SkuLockInfoServiceClientService skuLockInfoServiceClientService;

    @Test
    public void testLock(){
        skuLockInfoServiceClientService.invoke();

    }
    @Test
    public void testUnlock(){

    }
    @Test
    public void testDeduct(){

    }
    @Test
    public void testRestore(){

    }
}
