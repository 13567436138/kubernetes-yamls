package com.aosom.service.skustockdb;

import com.alibaba.fastjson.JSONArray;
import com.aosom.aosombase.grpc.RpcSeataServerInterceptor;
import com.aosom.aosombase.grpc.SellerContext;
import com.aosom.aosombase.shardingsphere.AosomSnowFlakeShardingKeyGenerator;
import com.aosom.constant.Constants;
import com.aosom.mapper.sellerstockdb.SellerSiteMapper;
import com.aosom.mapper.sellerstockdb.SellerSiteStockMapper;
import com.aosom.mapper.sellerstockdb.SellerStockPolicyMapper;
import com.aosom.mapper.sellerstockdb.StocksMapper;
import com.aosom.mapper.skustockdb.SkuLockInfoMapper;
import com.aosom.mapper.skustockdb.SkuStockInfoMapper;
import com.aosom.mapper.stocklogdb.StockLogMapper;
import com.aosom.model.*;
import com.aosom.service.stock.*;
import com.aosom.utils.LocationUtils;
import io.grpc.stub.StreamObserver;
import io.seata.spring.annotation.GlobalTransactional;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/24 19:11
 * @Description:
 */
@GrpcService(interceptors = {RpcSeataServerInterceptor.class})
public class SkuLockInfoServiceImpl extends SkuLockInfoServiceGrpc.SkuLockInfoServiceImplBase {
    @Autowired
    private AosomSnowFlakeShardingKeyGenerator keyGenerator;
    @Autowired
    private SkuLockInfoMapper skuLockInfoMapper;
    @Autowired
    private SkuStockInfoMapper skuStockInfoMapper;
    @Autowired
    private SellerStockPolicyMapper sellerStockPolicyMapper;
    @Autowired
    private SellerSiteMapper sellerSiteMapper;
    @Autowired
    private SellerSiteStockMapper sellerSiteStockMapper;
    @Autowired
    private StockLogMapper stockLogMapper;
    @Autowired
    private StocksMapper stocksMapper;
    /**
     * 执行锁定操作
     * @param request
     * @param stockIds
     */
    private String doLock(SkuLockInfoListRequest requestList,SkuLockInfoRequest request,List<Long> stockIds){
        int lockNum=request.getLocknum();
        String loginfo="{";
        for(int i=0;i<stockIds.size();i++){
            Long stockid=stockIds.get(i);
            loginfo+=stockid+":{num:";

            SkuStockInfo skuStockInfo=skuStockInfoMapper.getBySeleridAndSkuidAndStockid(request.getSellerid(),request.getSkuid(),stockid);
            if(skuStockInfo==null){
                continue;
            }
            int num=skuStockInfo.getRealnum()-skuStockInfo.getLocknum();
            SkuLockInfo skuLockInfo=new SkuLockInfo();
            SkuLockInfo dbObj=skuLockInfoMapper.getByOrderidAndSellerIdAndSkuidAndStockId(requestList.getOrderid(),
                    request.getSellerid(),request.getSkuid(),stockid);
            if(num>=lockNum){
                if(dbObj==null) {
                    skuLockInfo.setLocknum(lockNum);
                    skuLockInfo.setSellerid(request.getSellerid());
                    skuLockInfo.setId((Long) keyGenerator.generateKey());
                    skuLockInfo.setOrderid(requestList.getOrderid());
                    skuLockInfo.setSkuid(request.getSkuid());
                    skuLockInfo.setStockid(stockid);
                    skuLockInfo.setCt(new Date());
                    skuLockInfo.setUt(new Date());
                    skuLockInfoMapper.insert(skuLockInfo);
                }else{
                    dbObj.setLocknum(lockNum);
                    skuLockInfoMapper.updateLockNumById(dbObj);
                }

                skuStockInfo.setLocknum(skuStockInfo.getLocknum()+lockNum);
                skuStockInfoMapper.updateByPrimaryKey(skuStockInfo);

                loginfo+=lockNum+"},";
                break;
            }else{
                lockNum=lockNum-num;

                if(dbObj==null) {
                    skuLockInfo.setSellerid(request.getSellerid());
                    skuLockInfo.setId((Long)keyGenerator.generateKey());
                    skuLockInfo.setLocknum(num);
                    skuLockInfo.setOrderid(requestList.getOrderid());
                    skuLockInfo.setSkuid(request.getSkuid());
                    skuLockInfo.setStockid(stockid);
                    skuLockInfo.setCt(new Date());
                    skuLockInfo.setUt(new Date());
                    skuLockInfoMapper.insert(skuLockInfo);
                }else{
                    dbObj.setLocknum(num);
                    skuLockInfoMapper.updateLockNumById(dbObj);
                }


                skuStockInfo.setLocknum(skuStockInfo.getLocknum()+num);
                skuStockInfoMapper.updateByPrimaryKey(skuStockInfo);

                loginfo+=num+"},";
            }

        }
        loginfo=loginfo.substring(0,loginfo.length()-1);
        loginfo+="}";
        return loginfo;
    }

    /**
     * 排序
     * @param requestList
     * @param stocksList
     * @return
     */
    private List<Stocks> orderStocks(SkuLockInfoListRequest requestList, List<Stocks> stocksList,List<Long> stockIds){
        List<Stocks> ret=new ArrayList<>();
        List<Double> doubleList=new ArrayList<>();
        for(int j=0;j<stocksList.size();j++){
            boolean contains=false;
            for(int x=0;x<stockIds.size();x++){
                if(stocksList.get(j).getId()==stockIds.get(x)){
                    contains=true;
                    break;
                }
            }

            if(contains){
                Double distance= LocationUtils.getDistance(requestList.getLat(),requestList.getLng(),stocksList.get(j).getLat().doubleValue(),stocksList.get(j).getLng().doubleValue());
                int index=0;
                for(int i=0;i<doubleList.size();i++){
                    if(doubleList.get(i)<distance){
                        index++;
                    }
                }
                doubleList.add(index,distance);
                ret.add(index,stocksList.get(j));
            }

        }
        return ret;
    }

    /**
     * 锁定库存
     * @param requestList
     * @param responseObserver
     */
    @Override
    public void lock(SkuLockInfoListRequest requestList, StreamObserver<SkuLockInfoResponse> responseObserver) {
        Long loginSeller=SellerContext.getSellerId();
        String loginfo="{";
        for(int i=0;i<requestList.getSkuLockInfoRequestList().size();i++) {
            SkuLockInfoRequest request=requestList.getSkuLockInfoRequest(i);
            loginfo+=request.getSkuid()+":{num:"+request.getLocknum()+",";
            SellerSite sellersite = sellerSiteMapper.getBySiteidAndSellerid(requestList.getSiteid(), request.getSellerid());
            int mode = sellersite.getMode();
            switch (mode) {
                case 1://根据站点仓库的优先级，优先级高的先选
                    List<SellerSiteStock> sellerSiteStockList = sellerSiteStockMapper.findBySiteidAndSelleridOrdered(requestList.getSiteid(), request.getSellerid());

                    if (sellerSiteStockList != null && sellerSiteStockList.size() > 0) {
                        List<Long> stockIds = new ArrayList<>();
                        for (SellerSiteStock sellerSiteStock : sellerSiteStockList) {
                            stockIds.add(sellerSiteStock.getStockid());
                        }
                        boolean check = skuStockInfoMapper.checkIfHaveEnoughSkuStockInfo(stockIds, request.getSkuid(), request.getSellerid(), request.getLocknum());
                        if (!check) {
                            SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(false).setMsg("库存数量不够").build();
                            responseObserver.onNext(skuLockInfoResponse);
                            responseObserver.onCompleted();
                            return;
                        }
                        loginfo+=doLock(requestList,request, stockIds)+",";

                    } else {
                        SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(false).setMsg("商家站点发货仓库不存在").build();
                        responseObserver.onNext(skuLockInfoResponse);
                        responseObserver.onCompleted();
                        return;
                    }
                    break;

                case 2://根据用户收货地址与仓库的距离 批发自提时，无法确定距离，可让用户手工选择仓库（满足数量要求的仓库）

                    if (request.getManualStockIdsCount() > 0) {
                        boolean check = skuStockInfoMapper.checkIfHaveEnoughSkuStockInfo(request.getManualStockIdsList(), request.getSkuid(), request.getSellerid(), request.getLocknum());
                        if (!check) {
                            SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(false).setMsg("库存数量不够").build();
                            responseObserver.onNext(skuLockInfoResponse);
                            responseObserver.onCompleted();
                            return;
                        }
                        loginfo+=doLock(requestList,request, request.getManualStockIdsList())+",";

                    } else {
                        List<Stocks> stocksList = stocksMapper.findSellerStocks(request.getSellerid());
                        List<Long> stockIds = new ArrayList<>();
                        for (Stocks stocks : stocksList) {
                            stockIds.add(stocks.getId());
                        }

                        stockIds=skuStockInfoMapper.findBySkuIdAndSelleridAndStockIds(request.getSkuid(),request.getSellerid(),stockIds);
                        stocksList=orderStocks(requestList,stocksList,stockIds);

                        stockIds.clear();
                        for (Stocks stocks : stocksList) {
                            stockIds.add(stocks.getId());
                        }
                        boolean check = skuStockInfoMapper.checkIfHaveEnoughSkuStockInfo(stockIds, request.getSkuid(), request.getSellerid(), request.getLocknum());
                        if (!check) {
                            SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(false).setMsg("库存数量不够").build();
                            responseObserver.onNext(skuLockInfoResponse);
                            responseObserver.onCompleted();
                            return;
                        }
                        loginfo+=doLock(requestList,request, stockIds)+",";

                    }
                    break;
                case 3://设置不同区域的库存调度优先级，如：设置州维度的仓库优先级
                    SellerStockPolicy sellerStockPolicy = sellerStockPolicyMapper.getBySelleridAndSiteidAndCountrycodeAndStateCodeAndCountyCode(request.getSellerid(), requestList.getSiteid(), request.getCountrycode(), request.getStatecode(), request.getCountycode());
                    if (sellerStockPolicy == null) {
                        SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(false).setMsg("国别/州/县调度策略不存在").build();
                        responseObserver.onNext(skuLockInfoResponse);
                        responseObserver.onCompleted();
                        return;
                    }
                    String stockIds = sellerStockPolicy.getStockidlist();
                    JSONArray jsonarray = JSONArray.parseArray(stockIds);
                    List<Long> stockids = jsonarray.toJavaList(Long.class);
                    loginfo+=doLock(requestList,request, stockids);

                    break;
                default:
                    SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(false).setMsg("商家发货模式错误").build();
                    responseObserver.onNext(skuLockInfoResponse);
                    responseObserver.onCompleted();
                    return;
            }
        }
        loginfo=loginfo.substring(0,loginfo.length()-1);
        loginfo+="}}";
        StockLog log=new StockLog();
        log.setId((Long)keyGenerator.generateKey());
        log.setFrom(Constants.STOCK_LOG_FROM_OMS);
        log.setCt(new Date());
        log.setUt(new Date());
        log.setOptype(Constants.STOCK_LOG_OPT_TYPE_LOCK);
        log.setUid(requestList.getUid());
        log.setOrderid(requestList.getOrderid());
        log.setSiteid(requestList.getSiteid());
        log.setSkustockinfo(loginfo);
        stockLogMapper.insert(log);


        SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(true).setMsg("锁定成功").build();
        responseObserver.onNext(skuLockInfoResponse);
        responseObserver.onCompleted();

        int a=1/0;
    }

    /**
     *解锁库存呢
     * @param request
     * @param responseObserver
     */
    @Transactional(rollbackFor=RuntimeException.class)
    @Override
    public void unlock(OrderUnlockRequest request, StreamObserver<SkuLockInfoResponse> responseObserver) {
        List<SkuLockInfo>skuLockInfoList=skuLockInfoMapper.findByOrderId(request.getOrderid());
        List<SkuLockInfo>skuLockInfoGroupList=skuLockInfoMapper.findByOrderIdGroupBySkuid(request.getOrderid());
        String loginfo="{";
        for(int j=0;j<skuLockInfoGroupList.size();j++) {
            loginfo+=skuLockInfoGroupList.get(j).getSkuid()+"{"+skuLockInfoGroupList.get(j).getLocknum()+",[";
            for (int i = 0; i < skuLockInfoList.size(); i++) {
                if(skuLockInfoGroupList.get(j).getSkuid()==skuLockInfoList.get(i).getSkuid()) {
                    SkuLockInfo skuLockInfo = skuLockInfoList.get(i);
                    loginfo+=skuLockInfo.getStockid()+":{"+skuLockInfo.getLocknum()+"},";
                    skuStockInfoMapper.unLockNum(skuLockInfo.getSellerid(), skuLockInfo.getSkuid(), skuLockInfo.getStockid(), skuLockInfo.getLocknum());
                }
            }
            loginfo=loginfo.substring(0,loginfo.length());
        }
        loginfo=loginfo.substring(0,loginfo.length()-1);
        loginfo+="}";

        StockLog log=new StockLog();
        log.setFrom(Constants.STOCK_LOG_FROM_OMS);
        log.setCt(new Date());
        log.setUt(new Date());
        log.setOptype(Constants.STOCK_LOG_OPT_TYPE_UNLOCK);
        log.setUid(request.getUid());
        log.setOrderid(request.getOrderid());
        log.setSiteid(request.getSiteid());
        log.setSkustockinfo(loginfo);
        stockLogMapper.insert(log);

        SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(true).setMsg("解锁成功").build();
        responseObserver.onNext(skuLockInfoResponse);
        responseObserver.onCompleted();
        return;
    }

    /**
     * 出货扣除库存
     * @param request
     * @param responseObserver
     */
    @Override
    public void deduct(OrderDeductRequest request, StreamObserver<SkuLockInfoResponse> responseObserver) {
        List<SkuLockInfo>skuLockInfoList=skuLockInfoMapper.findByOrderId(request.getOrderid());
        List<SkuLockInfo>skuLockInfoGroupList=skuLockInfoMapper.findByOrderIdGroupBySkuid(request.getOrderid());
        String loginfo="{";
        for(int j=0;j<skuLockInfoGroupList.size();j++) {
            loginfo+=skuLockInfoGroupList.get(j).getSkuid()+"{"+skuLockInfoGroupList.get(j).getLocknum()+",[";
            for (int i = 0; i < skuLockInfoList.size(); i++) {
                if(skuLockInfoGroupList.get(j).getSkuid()==skuLockInfoList.get(i).getSkuid()) {
                    SkuLockInfo skuLockInfo = skuLockInfoList.get(i);
                    loginfo+=skuLockInfo.getStockid()+":{"+skuLockInfo.getLocknum()+"},";
                    skuStockInfoMapper.deduct(skuLockInfo.getSellerid(), skuLockInfo.getSkuid(), skuLockInfo.getStockid(), skuLockInfo.getLocknum());
                }
            }
            loginfo=loginfo.substring(0,loginfo.length());
        }
        loginfo=loginfo.substring(0,loginfo.length()-1);
        loginfo+="}";

        StockLog log=new StockLog();
        log.setFrom(Constants.STOCK_LOG_FROM_OMS);
        log.setCt(new Date());
        log.setUt(new Date());
        log.setOptype(Constants.STOCK_LOG_OPT_TYPE_SUBSTRACT);
        log.setUid(request.getUid());
        log.setOrderid(request.getOrderid());
        log.setSiteid(request.getSiteid());
        log.setSkustockinfo(loginfo);
        stockLogMapper.insert(log);

        SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(true).setMsg("解锁成功").build();
        responseObserver.onNext(skuLockInfoResponse);
        responseObserver.onCompleted();
        return;
    }

    /**
     * 商品回退
     * @param request
     * @param responseObserver
     */
    @Override
    public void restore(RestoreListRequest request, StreamObserver<SkuLockInfoResponse> responseObserver) {
        List<RestoreRequest>restoreRequestList=request.getRestoreRequestList();
        String loginfo="{";
        for(int i=0;i<restoreRequestList.size();i++){
            RestoreRequest restoreRequest=restoreRequestList.get(i);
            loginfo+=restoreRequest.getSkuid()+":{"+restoreRequest.getRestoreNum()+","+restoreRequest.getStockid()+"},";
            skuStockInfoMapper.restore(restoreRequest.getSellerid(), restoreRequest.getSkuid(), restoreRequest.getStockid(), restoreRequest.getRestoreNum());
        }
        loginfo=loginfo.substring(0,loginfo.length());
        loginfo+="}";

        StockLog log=new StockLog();
        log.setFrom(Constants.STOCK_LOG_FROM_OMS);
        log.setCt(new Date());
        log.setUt(new Date());
        log.setOptype(Constants.STOCK_LOG_OPT_TYPE_ADD);
        log.setUid(request.getUid());
        log.setOrderid(request.getOrderid());
        log.setSiteid(request.getSiteid());
        log.setSkustockinfo(loginfo);
        stockLogMapper.insert(log);

        SkuLockInfoResponse skuLockInfoResponse = SkuLockInfoResponse.newBuilder().setIsSuccess(true).setMsg("解锁成功").build();
        responseObserver.onNext(skuLockInfoResponse);
        responseObserver.onCompleted();
        return;
    }
}
