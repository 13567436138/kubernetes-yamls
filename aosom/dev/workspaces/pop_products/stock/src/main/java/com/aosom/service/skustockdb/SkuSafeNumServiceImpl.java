package com.aosom.service.skustockdb;

import com.aosom.aosombase.grpc.RpcSeataServerInterceptor;
import com.aosom.aosombase.shardingsphere.AosomSnowFlakeShardingKeyGenerator;
import com.aosom.dto.SkuSafeNumDto;
import com.aosom.mapper.skustockdb.SkuSafeNumMapper;
import com.aosom.model.SkuSafeNum;
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
public class SkuSafeNumServiceImpl extends SkuSafeNumServiceGrpc.SkuSafeNumServiceImplBase {
    @Autowired
    private SkuSafeNumMapper skuSafeNumMapper;
    @Autowired
    private AosomSnowFlakeShardingKeyGenerator keyGenerator;

    @Override
    public void delete(SkuSafeNumRequest request, StreamObserver<SkuSafeNumResponse> responseObserver) {
        skuSafeNumMapper.deleteByPrimaryKey(request.getId());
        SkuSafeNumResponse skuSafeNumResponse = SkuSafeNumResponse.newBuilder().setIsSuccess(true).setMsg("删除成功").build();
        responseObserver.onNext(skuSafeNumResponse);
        responseObserver.onCompleted();
        return;
    }

    @Override
    public void add(SkuSafeNumRequest request, StreamObserver<SkuSafeNumResponse> responseObserver) {
        SkuSafeNum skuSafeNum=new SkuSafeNum();
        skuSafeNum.setId((Long)keyGenerator.generateKey());
        skuSafeNum.setSafenum(request.getSafenum());
        skuSafeNum.setSiteid(request.getSiteid());
        skuSafeNum.setSkuid(request.getSkuid());
        skuSafeNum.setCt(new Date());
        skuSafeNum.setUt(new Date());
        skuSafeNumMapper.insert(skuSafeNum);
        SkuSafeNumResponse skuSafeNumResponse = SkuSafeNumResponse.newBuilder().setIsSuccess(true).setMsg("添加成功").build();
        responseObserver.onNext(skuSafeNumResponse);
        responseObserver.onCompleted();
        return;
    }

    @Override
    public void update(SkuSafeNumRequest request, StreamObserver<SkuSafeNumResponse> responseObserver) {
        SkuSafeNum skuSafeNum=new SkuSafeNum();
        skuSafeNum.setId(request.getId());
        skuSafeNum.setSafenum(request.getSafenum());
        skuSafeNum.setSiteid(request.getSiteid());
        skuSafeNum.setSkuid(request.getSkuid());
        skuSafeNum.setUt(new Date());
        skuSafeNumMapper.updateByPrimaryKey(skuSafeNum);
        SkuSafeNumResponse skuSafeNumResponse = SkuSafeNumResponse.newBuilder().setIsSuccess(true).setMsg("更新成功").build();
        responseObserver.onNext(skuSafeNumResponse);
        responseObserver.onCompleted();
        return;
    }

    @Override
    public void get(SkuSafeNumRequest request, StreamObserver<SkuSafeNumEntity> responseObserver) {
        SkuSafeNum skuSafeNum=skuSafeNumMapper.selectByPrimaryKey(request.getId());
        SkuSafeNumEntity skuSafeNumEntity = SkuSafeNumEntity.newBuilder().setId(skuSafeNum.getId())
                .setSafenum(skuSafeNum.getSafenum()).setSiteid(skuSafeNum.getSiteid())
                .setSkuid(skuSafeNum.getSkuid()).build();
        responseObserver.onNext(skuSafeNumEntity);
        responseObserver.onCompleted();
        return;
    }

    @Override
    public void findAll(SkuSafeNumRequest request, StreamObserver<SkuSafeNumEntityListResponse> responseObserver) {
        SkuSafeNumDto skuSafeNumDto=new SkuSafeNumDto();
        skuSafeNumDto.setId(request.getId());
        skuSafeNumDto.setSafenumEnd(request.getSafenumEnd());
        skuSafeNumDto.setSafenumStart(request.getSafenumStart());
        skuSafeNumDto.setSiteid(request.getSiteid());
        skuSafeNumDto.setSkuid(request.getSkuid());
        List<SkuSafeNum>skuSafeNumList= skuSafeNumMapper.findByCondition(skuSafeNumDto);
        SkuSafeNumEntityListResponse.Builder skuSafeNumEntityListResponseBuilder=SkuSafeNumEntityListResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功");
        for(int i=0;i<skuSafeNumList.size();i++){
            SkuSafeNum skuSafeNum=skuSafeNumList.get(i);
            SkuSafeNumEntity skuSafeNumEntity= SkuSafeNumEntity.newBuilder().setId(skuSafeNum.getId())
                    .setSafenum(skuSafeNum.getSafenum()).setSiteid(skuSafeNum.getSiteid())
                    .setSkuid(skuSafeNum.getSkuid()).build();
            skuSafeNumEntityListResponseBuilder.addSkuSafeNumEntity(skuSafeNumEntity);
        }
        responseObserver.onNext(skuSafeNumEntityListResponseBuilder.build());
        responseObserver.onCompleted();
        return;
    }

    @Override
    public void findPage(SkuSafeNumRequest request, StreamObserver<SkuSafeNumPageResponse> responseObserver) {
        SkuSafeNumDto skuSafeNumDto=new SkuSafeNumDto();
        skuSafeNumDto.setId(request.getId()==0?null:request.getId());
        skuSafeNumDto.setSafenumEnd(request.getSafenumEnd());
        skuSafeNumDto.setSafenumStart(request.getSafenumStart());
        skuSafeNumDto.setSiteid(request.getSiteid()==0?null:request.getSiteid());
        skuSafeNumDto.setSkuid(request.getSkuid()==0?null:request.getSkuid());
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        PageInfo<SkuSafeNum> pageInfo = new PageInfo<SkuSafeNum>(skuSafeNumMapper.findByCondition(skuSafeNumDto));
        List<SkuSafeNum>skuSafeNumList=pageInfo.getList() ;
        Long total=pageInfo.getTotal();
        SkuSafeNumPageResponse.Builder builder=SkuSafeNumPageResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功").setCurrentPage(request.getCurrentPage())
                .setPageSize(request.getPageSize()).setTotal(total);
        for(int i=0;i<skuSafeNumList.size();i++){
            SkuSafeNum skuSafeNum=skuSafeNumList.get(i);
            SkuSafeNumEntity skuSafeNumEntity= SkuSafeNumEntity.newBuilder().setId(skuSafeNum.getId())
                    .setSafenum(skuSafeNum.getSafenum()).setSiteid(skuSafeNum.getSiteid())
                    .setSkuid(skuSafeNum.getSkuid()).build();
            builder.addData(skuSafeNumEntity);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
        return;
    }
}
