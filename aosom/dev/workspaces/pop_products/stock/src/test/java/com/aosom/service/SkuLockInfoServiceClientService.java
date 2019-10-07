package com.aosom.service;

import com.aosom.aosombase.grpc.RpcSeataClientInterceptor;
import com.aosom.aosombase.grpc.SellerContext;
import com.aosom.service.stock.SkuLockInfoListRequest;
import com.aosom.service.stock.SkuLockInfoRequest;
import com.aosom.service.stock.SkuLockInfoResponse;
import com.aosom.service.stock.SkuLockInfoServiceGrpc;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/27 15:15
 * @Description:
 */
@Component
public class SkuLockInfoServiceClientService {
    @GrpcClient(value = "stockService",interceptors = {RpcSeataClientInterceptor.class})
    private SkuLockInfoServiceGrpc.SkuLockInfoServiceBlockingStub  simpleBlockingStub;

    @GlobalTransactional
    public void invoke(){
        SellerContext.setSellerId(1L);

        SkuLockInfoListRequest.Builder request=SkuLockInfoListRequest.newBuilder()
                .setUid(1).setOrderid(2).setSiteid(100)
                .setLat(10).setLng(22);
        SkuLockInfoRequest skuLockInfoRequest=SkuLockInfoRequest.newBuilder()
                .setSkuid(6).setLocknum(1).setSellerid(1).setCountrycode("CN")
                .setStatecode("ZJ").setCountycode("XS").build();
        request.addSkuLockInfoRequest(skuLockInfoRequest);
        String a= RootContext.getXID();
        SkuLockInfoResponse response=simpleBlockingStub.lock(request.build());
        System.out.println(response.getIsSuccess());

    }
}
