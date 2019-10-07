package com.aosom.service.sellerstockdb;

import com.aosom.aosombase.grpc.RpcSeataServerInterceptor;
import com.aosom.aosombase.shardingsphere.AosomSnowFlakeShardingKeyGenerator;
import com.aosom.mapper.sellerstockdb.SellerSiteMapper;
import com.aosom.model.SellerSite;
import com.aosom.model.SellerSiteStock;
import com.aosom.service.stock.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/24 19:09
 * @Description:
 */
@GrpcService(interceptors = {RpcSeataServerInterceptor.class})
public class SellerSiteServiceImpl extends SellerSiteServiceGrpc.SellerSiteServiceImplBase {
    @Autowired
    private AosomSnowFlakeShardingKeyGenerator keyGenerator;
    @Autowired
    private SellerSiteMapper sellerSiteMapper;

    @Override
    public void delete(SellerSiteRequest request, StreamObserver<SellerSiteResponse> responseObserver) {
        sellerSiteMapper.deleteByPrimaryKey(request.getId());
        SellerSiteResponse sellerSiteResponse = SellerSiteResponse.newBuilder().setIsSuccess(true).setMsg("删除成功").build();
        responseObserver.onNext(sellerSiteResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void add(SellerSiteRequest request, StreamObserver<SellerSiteResponse> responseObserver) {
        SellerSite sellerSite=new SellerSite();
        sellerSite.setId((Long)keyGenerator.generateKey());
        sellerSite.setMode(request.getMode());
        sellerSite.setSellerid(request.getSellerid());
        sellerSite.setSiteid(request.getSiteid());

        sellerSiteMapper.insert(sellerSite);
        SellerSiteResponse sellerSiteResponse = SellerSiteResponse.newBuilder().setIsSuccess(true).setMsg("添加成功").build();
        responseObserver.onNext(sellerSiteResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void update(SellerSiteRequest request, StreamObserver<SellerSiteResponse> responseObserver) {
        SellerSite sellerSite=new SellerSite();
        sellerSite.setId(request.getId());
        sellerSite.setMode(request.getMode());
        sellerSite.setSellerid(request.getSellerid());
        sellerSite.setSiteid(request.getSiteid());

        sellerSiteMapper.updateByPrimaryKey(sellerSite);
        SellerSiteResponse sellerSiteResponse = SellerSiteResponse.newBuilder().setIsSuccess(true).setMsg("修改成功").build();
        responseObserver.onNext(sellerSiteResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void get(SellerSiteRequest request, StreamObserver<SellerSiteEntity> responseObserver) {
        SellerSite sellerSite=sellerSiteMapper.selectByPrimaryKey(request.getId());
        SellerSiteEntity sellerSiteEntity = SellerSiteEntity.newBuilder()
                .setId(sellerSite.getId()).setMode(sellerSite.getMode())
                .setSellerid(sellerSite.getSellerid())
                .setSiteid(sellerSite.getSiteid())
                .build();
        responseObserver.onNext(sellerSiteEntity);
        responseObserver.onCompleted();
    }

    @Override
    public void findPage(SellerSiteRequest request, StreamObserver<SellerSitePageResponse> responseObserver) {
        SellerSite site=new SellerSite();
        site.setSellerid(request.getSellerid());
        site.setMode(request.getMode());
        site.setSiteid(request.getSiteid());
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        PageInfo<SellerSite> pageInfo = new PageInfo<SellerSite>(sellerSiteMapper.findByCondition(site));
        List<SellerSite> sellerSites=pageInfo.getList() ;
        Long total=pageInfo.getTotal();
        SellerSitePageResponse.Builder builder=SellerSitePageResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功").setCurrentPage(request.getCurrentPage())
                .setPageSize(request.getPageSize()).setTotal(total);
        for(int i=0;i<sellerSites.size();i++){
            SellerSite sellerSite=sellerSites.get(i);
            SellerSiteEntity sellerSiteEntity= SellerSiteEntity.newBuilder()
                    .setId(sellerSite.getId()).setMode(sellerSite.getMode())
                    .setSellerid(sellerSite.getSellerid())
                    .setSiteid(sellerSite.getSiteid())
                    .build();
            builder.addData(sellerSiteEntity);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
