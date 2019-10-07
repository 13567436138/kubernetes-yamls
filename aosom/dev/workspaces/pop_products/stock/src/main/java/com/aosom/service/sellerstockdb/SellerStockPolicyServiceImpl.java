package com.aosom.service.sellerstockdb;

import com.aosom.aosombase.grpc.RpcSeataServerInterceptor;
import com.aosom.aosombase.shardingsphere.AosomSnowFlakeShardingKeyGenerator;
import com.aosom.mapper.sellerstockdb.SellerStockPolicyMapper;
import com.aosom.model.SellerStockPolicy;
import com.aosom.model.SkuStockInfo;
import com.aosom.service.stock.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/24 19:10
 * @Description:
 */
@GrpcService(interceptors = {RpcSeataServerInterceptor.class})
public class SellerStockPolicyServiceImpl extends SellerStockPolicyServiceGrpc.SellerStockPolicyServiceImplBase {
    @Autowired
    private AosomSnowFlakeShardingKeyGenerator keyGenerator;
    @Autowired
    private SellerStockPolicyMapper sellerStockPolicyMapper;

    @Override
    public void delete(SellerStockPolicyRequest request, StreamObserver<SellerStockPolicyResponse> responseObserver) {
        sellerStockPolicyMapper.deleteByPrimaryKey(request.getId());
        SellerStockPolicyResponse sellerStockPolicyResponse = SellerStockPolicyResponse.newBuilder().setIsSuccess(true).setMsg("删除成功").build();
        responseObserver.onNext(sellerStockPolicyResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void add(SellerStockPolicyRequest request, StreamObserver<SellerStockPolicyResponse> responseObserver) {
        SellerStockPolicy sellerStockPolicy=new SellerStockPolicy();
        sellerStockPolicy.setId((Long)keyGenerator.generateKey());
        sellerStockPolicy.setCountrycode(request.getCountrycode());
        sellerStockPolicy.setCountycode(request.getCountycode());
        sellerStockPolicy.setSellerid(request.getSellerid());
        sellerStockPolicy.setStatecode(request.getStatecode());
        sellerStockPolicy.setSiteid(request.getSiteid());
        sellerStockPolicy.setStockidlist(request.getStockidlist());
        sellerStockPolicy.setCt(new Date());
        sellerStockPolicy.setUt(new Date());
        sellerStockPolicyMapper.insert(sellerStockPolicy);
        SellerStockPolicyResponse sellerStockPolicyResponse = SellerStockPolicyResponse.newBuilder().setIsSuccess(true).setMsg("添加成功").build();
        responseObserver.onNext(sellerStockPolicyResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void update(SellerStockPolicyRequest request, StreamObserver<SellerStockPolicyResponse> responseObserver) {
        SellerStockPolicy sellerStockPolicy=new SellerStockPolicy();
        sellerStockPolicy.setId(request.getId());
        sellerStockPolicy.setCountrycode(request.getCountrycode());
        sellerStockPolicy.setCountycode(request.getCountycode());
        sellerStockPolicy.setSellerid(request.getSellerid());
        sellerStockPolicy.setStatecode(request.getStatecode());
        sellerStockPolicy.setSiteid(request.getSiteid());
        sellerStockPolicy.setStockidlist(request.getStockidlist());
        sellerStockPolicy.setUt(new Date());
        sellerStockPolicyMapper.updateByPrimaryKey(sellerStockPolicy);
        SellerStockPolicyResponse sellerStockPolicyResponse = SellerStockPolicyResponse.newBuilder().setIsSuccess(true).setMsg("修改成功").build();
        responseObserver.onNext(sellerStockPolicyResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void get(SellerStockPolicyRequest request, StreamObserver<SellerStockPolicyEntity> responseObserver) {
        SellerStockPolicy sellerStockPolicy=sellerStockPolicyMapper.selectByPrimaryKey(request.getId());
        SellerStockPolicyEntity sellerStockPolicyEntity = SellerStockPolicyEntity.newBuilder()
                .setId(sellerStockPolicy.getId()).setCountrycode(sellerStockPolicy.getCountrycode())
                .setCountycode(sellerStockPolicy.getCountycode())
                .setSellerid(sellerStockPolicy.getSellerid())
                .setStatecode(sellerStockPolicy.getStatecode())
                .setSiteid(sellerStockPolicy.getSiteid())
                .setStockidlist(sellerStockPolicy.getStockidlist())
                .build();
        responseObserver.onNext(sellerStockPolicyEntity);
        responseObserver.onCompleted();
    }

    @Override
    public void findPage(SellerStockPolicyRequest request, StreamObserver<SellerStockPolicyPageResponse> responseObserver) {
        SellerStockPolicy policy=new SellerStockPolicy();
        policy.setStatecode(request.getStatecode());
        policy.setCountrycode(request.getCountrycode());
        policy.setCountycode(request.getCountycode());
        policy.setSiteid(request.getSiteid());
        policy.setSellerid(request.getSellerid());
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        PageInfo<SellerStockPolicy> pageInfo = new PageInfo<SellerStockPolicy>(sellerStockPolicyMapper.findByCondition(policy));
        List<SellerStockPolicy> sellerStockPolicies=pageInfo.getList() ;
        Long total=pageInfo.getTotal();
        SellerStockPolicyPageResponse.Builder builder=SellerStockPolicyPageResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功").setCurrentPage(request.getCurrentPage())
                .setPageSize(request.getPageSize()).setTotal(total);
        for(int i=0;i<sellerStockPolicies.size();i++){
            SellerStockPolicy sellerStockPolicy=sellerStockPolicies.get(i);
            SellerStockPolicyEntity sellerStockPolicyEntity= SellerStockPolicyEntity.newBuilder()
                    .setId(sellerStockPolicy.getId()).setCountrycode(sellerStockPolicy.getCountrycode())
                    .setCountycode(sellerStockPolicy.getCountycode())
                    .setSellerid(sellerStockPolicy.getSellerid())
                    .setStatecode(sellerStockPolicy.getStatecode())
                    .setSiteid(sellerStockPolicy.getSiteid())
                    .setStockidlist(sellerStockPolicy.getStockidlist())
                    .build();
            builder.addData(sellerStockPolicyEntity);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
