package com.aosom.service.sellerstockdb;

import com.aosom.aosombase.grpc.RpcSeataServerInterceptor;
import com.aosom.aosombase.shardingsphere.AosomSnowFlakeShardingKeyGenerator;
import com.aosom.mapper.sellerstockdb.SellerSiteStockMapper;
import com.aosom.model.SellerSiteStock;
import com.aosom.model.SellerStockPolicy;
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
public class SellerSiteStockServiceImpl extends SellerSiteStockServiceGrpc.SellerSiteStockServiceImplBase {
    @Autowired
    private AosomSnowFlakeShardingKeyGenerator keyGenerator;

    @Autowired
    private SellerSiteStockMapper sellerSiteStockMapper;

    @Override
    public void delete(SellerSiteStockRequest request, StreamObserver<SellerSiteStockResponse> responseObserver) {
        sellerSiteStockMapper.deleteByPrimaryKey(request.getId());
        SellerSiteStockResponse sellerSiteStockResponse = SellerSiteStockResponse.newBuilder().setIsSuccess(true).setMsg("删除成功").build();
        responseObserver.onNext(sellerSiteStockResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void add(SellerSiteStockRequest request, StreamObserver<SellerSiteStockResponse> responseObserver) {
        SellerSiteStock sellerSiteStock=new SellerSiteStock();
        sellerSiteStock.setId((Long)keyGenerator.generateKey());
        sellerSiteStock.setPri((byte)request.getPri());
        sellerSiteStock.setSellerid(request.getSellerid());
        sellerSiteStock.setSiteid(request.getSiteid());
        sellerSiteStock.setStockid(request.getStockid());

        sellerSiteStockMapper.insert(sellerSiteStock);
        SellerSiteStockResponse sellerSiteStockResponse = SellerSiteStockResponse.newBuilder().setIsSuccess(true).setMsg("添加成功").build();
        responseObserver.onNext(sellerSiteStockResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void update(SellerSiteStockRequest request, StreamObserver<SellerSiteStockResponse> responseObserver) {
        SellerSiteStock sellerSiteStock=new SellerSiteStock();
        sellerSiteStock.setId(request.getId());
        sellerSiteStock.setPri((byte)request.getPri());
        sellerSiteStock.setSellerid(request.getSellerid());
        sellerSiteStock.setSiteid(request.getSiteid());
        sellerSiteStock.setStockid(request.getStockid());

        sellerSiteStockMapper.updateByPrimaryKey(sellerSiteStock);
        SellerSiteStockResponse sellerSiteStockResponse = SellerSiteStockResponse.newBuilder().setIsSuccess(true).setMsg("修改成功").build();
        responseObserver.onNext(sellerSiteStockResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void get(SellerSiteStockRequest request, StreamObserver<SellerSiteStockEntity> responseObserver) {
        SellerSiteStock sellerSiteStock=sellerSiteStockMapper.selectByPrimaryKey(request.getId());
        SellerSiteStockEntity sellerSiteStockEntity = SellerSiteStockEntity.newBuilder()
                .setId(sellerSiteStock.getId()).setPri(sellerSiteStock.getPri())
                .setSellerid(sellerSiteStock.getSellerid())
                .setStockid(sellerSiteStock.getStockid())
                .setSiteid(sellerSiteStock.getSiteid())
                .build();
        responseObserver.onNext(sellerSiteStockEntity);
        responseObserver.onCompleted();
    }

    @Override
    public void findPage(SellerSiteStockRequest request, StreamObserver<SellerSiteStockPageResponse> responseObserver) {
        SellerSiteStock siteStock=new SellerSiteStock();
        siteStock.setSellerid(request.getSellerid());
        siteStock.setStockid(request.getStockid());
        siteStock.setSiteid(request.getSiteid());
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        PageInfo<SellerSiteStock> pageInfo = new PageInfo<SellerSiteStock>(sellerSiteStockMapper.findByCondition(siteStock));
        List<SellerSiteStock> sellerSiteStocks=pageInfo.getList() ;
        Long total=pageInfo.getTotal();
        SellerSiteStockPageResponse.Builder builder=SellerSiteStockPageResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功").setCurrentPage(request.getCurrentPage())
                .setPageSize(request.getPageSize()).setTotal(total);
        for(int i=0;i<sellerSiteStocks.size();i++){
            SellerSiteStock sellerSiteStock=sellerSiteStocks.get(i);
            SellerSiteStockEntity sellerSiteStockEntity= SellerSiteStockEntity.newBuilder()
                    .setId(sellerSiteStock.getId()).setPri(sellerSiteStock.getPri())
                    .setSellerid(sellerSiteStock.getSellerid())
                    .setStockid(sellerSiteStock.getStockid())
                    .setSiteid(sellerSiteStock.getSiteid())
                    .build();
            builder.addData(sellerSiteStockEntity);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
