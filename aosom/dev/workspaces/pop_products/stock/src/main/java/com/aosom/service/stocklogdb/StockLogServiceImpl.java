package com.aosom.service.stocklogdb;

import com.aosom.aosombase.grpc.RpcSeataServerInterceptor;
import com.aosom.mapper.stocklogdb.StockLogMapper;
import com.aosom.model.SkuStockInfo;
import com.aosom.model.StockLog;
import com.aosom.service.stock.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/24 19:12
 * @Description:
 */
@GrpcService(interceptors = {RpcSeataServerInterceptor.class})
public class StockLogServiceImpl extends StockLogServiceGrpc.StockLogServiceImplBase {
    @Autowired
    private StockLogMapper stockLogMapper;

    @Override
    public void get(StockLogRequest request, StreamObserver<StockLogEntity> responseObserver) {
        StockLog stockLog=stockLogMapper.selectByPrimaryKey(request.getId());
        StockLogEntity stockLogEntity = StockLogEntity.newBuilder()
                .setId(stockLog.getId()).setFrom(stockLog.getFrom())
                .setOptype(stockLog.getOptype()).setOrderid(stockLog.getOrderid())
                .setSiteid(stockLog.getSiteid()).setUid(stockLog.getUid())
                .setSkustockinfo(stockLog.getSkustockinfo()).build();
        responseObserver.onNext(stockLogEntity);
        responseObserver.onCompleted();
    }

    @Override
    public void findPage(StockLogRequest request, StreamObserver<StockLogPageResponse> responseObserver) {
        StockLog log=new StockLog();
        log.setUid(request.getUid());
        log.setOrderid(request.getOrderid());
        log.setFrom((byte)request.getFrom());
        log.setOptype((byte)request.getOptype());
        log.setSiteid(request.getSiteid());
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        PageInfo<StockLog> pageInfo = new PageInfo<StockLog>(stockLogMapper.findByCondition(log));
        List<StockLog> stockLogs=pageInfo.getList() ;
        Long total=pageInfo.getTotal();
        StockLogPageResponse.Builder builder=StockLogPageResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功").setCurrentPage(request.getCurrentPage())
                .setPageSize(request.getPageSize()).setTotal(total);
        for(int i=0;i<stockLogs.size();i++){
            StockLog stockLog=stockLogs.get(i);
            StockLogEntity stockLogEntity = StockLogEntity.newBuilder()
                    .setId(stockLog.getId()).setFrom(stockLog.getFrom())
                    .setOptype(stockLog.getOptype()).setOrderid(stockLog.getOrderid())
                    .setSiteid(stockLog.getSiteid()).setUid(stockLog.getUid())
                    .setSkustockinfo(stockLog.getSkustockinfo()).build();
            builder.addData(stockLogEntity);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
