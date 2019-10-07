package com.aosom.service.skustockdb;

import com.aosom.aosombase.grpc.RpcSeataServerInterceptor;
import com.aosom.aosombase.shardingsphere.AosomSnowFlakeShardingKeyGenerator;
import com.aosom.dto.SkuSafeNumDto;
import com.aosom.mapper.skustockdb.SkuStockInfoMapper;
import com.aosom.model.SkuSafeNum;
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
 * @Date: 2019/9/24 19:11
 * @Description:
 */
@GrpcService(interceptors = {RpcSeataServerInterceptor.class})
public class SkuStockInfoServiceImpl extends SkuStockInfoServiceGrpc.SkuStockInfoServiceImplBase {
    @Autowired
    private SkuStockInfoMapper skuStockInfoMapper;
    @Autowired
    private AosomSnowFlakeShardingKeyGenerator keyGenerator;

    @Override
    public void delete(SkuStockInfoRequest request, StreamObserver<SkuStockInfoResponse> responseObserver) {
        skuStockInfoMapper.deleteByPrimaryKey(request.getId());
        SkuStockInfoResponse skuStockInfoResponse = SkuStockInfoResponse.newBuilder().setIsSuccess(true).setMsg("删除成功").build();
        responseObserver.onNext(skuStockInfoResponse);
        responseObserver.onCompleted();
        return;
    }

    @Override
    public void add(SkuStockInfoRequest request, StreamObserver<SkuStockInfoResponse> responseObserver) {
        SkuStockInfo skuStockInfo=new SkuStockInfo();
        skuStockInfo.setId((Long)keyGenerator.generateKey());
        skuStockInfo.setLocknum(0);
        skuStockInfo.setRealnum(request.getRealnum());
        skuStockInfo.setSellerid(request.getSellerid());
        skuStockInfo.setSkuid(request.getSkuid());
        skuStockInfo.setStockid(request.getStockid());
        skuStockInfo.setCt(new Date());
        skuStockInfo.setUt(new Date());
        skuStockInfoMapper.insert(skuStockInfo);
        SkuStockInfoResponse skuStockInfoResponse = SkuStockInfoResponse.newBuilder().setIsSuccess(true).setMsg("添加成功").build();
        responseObserver.onNext(skuStockInfoResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void update(SkuStockInfoRequest request, StreamObserver<SkuStockInfoResponse> responseObserver) {
        SkuStockInfo skuStockInfo=new SkuStockInfo();
        skuStockInfo.setId(request.getId());
        skuStockInfo.setLocknum(request.getLocknum());
        skuStockInfo.setRealnum(request.getRealnum());
        skuStockInfo.setSellerid(request.getSellerid());
        skuStockInfo.setSkuid(request.getSkuid());
        skuStockInfo.setStockid(request.getStockid());
        skuStockInfo.setUt(new Date());
        skuStockInfoMapper.updateByPrimaryKey(skuStockInfo);
        SkuStockInfoResponse skuStockInfoResponse = SkuStockInfoResponse.newBuilder().setIsSuccess(true).setMsg("修改成功").build();
        responseObserver.onNext(skuStockInfoResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void get(SkuStockInfoRequest request, StreamObserver<SkuStockInfoEntity> responseObserver) {
        SkuStockInfo skuStockInfo=skuStockInfoMapper.selectByPrimaryKey(request.getId());
        SkuStockInfoEntity skuStockInfoEntity = SkuStockInfoEntity.newBuilder()
                .setId(skuStockInfo.getId()).setLocknum(skuStockInfo.getLocknum())
                .setRealnum(skuStockInfo.getRealnum()).setSellerid(skuStockInfo.getSellerid())
                .setSkuid(skuStockInfo.getSkuid()).setStockid(skuStockInfo.getStockid())
                .build();
        responseObserver.onNext(skuStockInfoEntity);
        responseObserver.onCompleted();
    }

    @Override
    public void findPage(SkuStockInfoRequest request, StreamObserver<SkuStockInfoPageResponse> responseObserver) {
        SkuStockInfo stockInfo=new SkuStockInfo();
        stockInfo.setStockid(request.getStockid());
        stockInfo.setSkuid(request.getSkuid());
        stockInfo.setSellerid(request.getSellerid());
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        PageInfo<SkuStockInfo> pageInfo = new PageInfo<SkuStockInfo>(skuStockInfoMapper.findByCondition(stockInfo));
        List<SkuStockInfo> skuStockInfos=pageInfo.getList() ;
        Long total=pageInfo.getTotal();
        SkuStockInfoPageResponse.Builder builder=SkuStockInfoPageResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功").setCurrentPage(request.getCurrentPage())
                .setPageSize(request.getPageSize()).setTotal(total);
        for(int i=0;i<skuStockInfos.size();i++){
            SkuStockInfo skuStockInfo=skuStockInfos.get(i);
            SkuStockInfoEntity skuStockInfoEntity= SkuStockInfoEntity.newBuilder()
                    .setId(skuStockInfo.getId()).setLocknum(skuStockInfo.getLocknum())
                    .setRealnum(skuStockInfo.getRealnum()).setSellerid(skuStockInfo.getSellerid())
                    .setSkuid(skuStockInfo.getSkuid()).setStockid(skuStockInfo.getStockid())
                    .build();
            builder.addData(skuStockInfoEntity);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void importStockInfo(SkuStockInfoImportRequest request, StreamObserver<SkuStockInfoResponse> responseObserver) {
        List<SkuStockInfoEntity> skuStockInfoEntityList=request.getDataList();
        for(int i=0;i<skuStockInfoEntityList.size();i++){

        }
    }

    @Override
    public void exportStockInfo(SkuStockInfoRequest request, StreamObserver<SkuStockInfoExportResponse> responseObserver) {

    }
}
