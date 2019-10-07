package com.aosom.service.sellerstockdb;

import com.aosom.aosombase.grpc.RpcSeataServerInterceptor;
import com.aosom.aosombase.shardingsphere.AosomSnowFlakeShardingKeyGenerator;
import com.aosom.dto.SkuSafeNumDto;
import com.aosom.mapper.sellerstockdb.StocksMapper;
import com.aosom.model.SkuSafeNum;
import com.aosom.model.Stocks;
import com.aosom.service.stock.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@GrpcService(interceptors = {RpcSeataServerInterceptor.class})
public class StockServiceImpl extends StocksServiceGrpc.StocksServiceImplBase {
    @Autowired
    private StocksMapper stocksMapper;
    @Autowired
    private AosomSnowFlakeShardingKeyGenerator keyGenerator;

    @Override
    public void delete(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        stocksMapper.deleteByPrimaryKey(request.getId());
        StockResponse stockResponse = StockResponse.newBuilder().setIsSuccess(true).setMsg("删除成功").build();
        responseObserver.onNext(stockResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void add(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        Stocks stocks=new Stocks();
        stocks.setId((Long)keyGenerator.generateKey());
        stocks.setAddress(request.getAddress());
        stocks.setCitycode(request.getCitycode());
        stocks.setCityname(request.getCityname());
        stocks.setCountry(request.getCountry());
        stocks.setCountrycode(request.getCountrycode());
        stocks.setCountycode(request.getCountycode());
        stocks.setCountyname(request.getCountyname());
        stocks.setCt(new Date());
        stocks.setUt(new Date());
        stocks.setLat(request.getLat());
        stocks.setLng(request.getLng());
        stocks.setName(request.getName());
        stocks.setPri(request.getPri());
        stocks.setSellerid(request.getSellerid());
        stocks.setStatecode(request.getStatecode());
        stocks.setStatename(request.getStatename());
        stocks.setZipcode(request.getZipcode());
        stocksMapper.insert(stocks);

        StockResponse stockResponse = StockResponse.newBuilder().setIsSuccess(true).setMsg("新增成功").build();
        responseObserver.onNext(stockResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void update(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        Stocks stocks=new Stocks();
        stocks.setAddress(request.getAddress());
        stocks.setCitycode(request.getCitycode());
        stocks.setCityname(request.getCityname());
        stocks.setCountry(request.getCountry());
        stocks.setCountrycode(request.getCountrycode());
        stocks.setCountycode(request.getCountycode());
        stocks.setCountyname(request.getCountyname());
        stocks.setCt(new Date());
        stocks.setUt(new Date());
        stocks.setLat(request.getLat());
        stocks.setLng(request.getLng());
        stocks.setName(request.getName());
        stocks.setPri(request.getPri());
        stocks.setSellerid(request.getSellerid());
        stocks.setStatecode(request.getStatecode());
        stocks.setStatename(request.getStatename());
        stocks.setZipcode(request.getZipcode());
        stocks.setId(request.getId());
        stocksMapper.updateByPrimaryKey(stocks);

        StockResponse stockResponse = StockResponse.newBuilder().setIsSuccess(true).setMsg("修改成功").build();
        responseObserver.onNext(stockResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void get(StockRequest request, StreamObserver<StockEntity> responseObserver) {
        Stocks stocks=stocksMapper.selectByPrimaryKey(request.getId());
        StockEntity stockEntity=StockEntity.newBuilder().setAddress(stocks.getAddress())
                .setCitycode(stocks.getCitycode()).setCityname(stocks.getCityname())
                .setCountry(stocks.getCountry()).setCountrycode(stocks.getCountrycode())
                .setCountycode(stocks.getCountycode()).setCountyname(stocks.getCountyname())
                .setId(stocks.getId()).setZipcode(stocks.getZipcode())
                .setStatename(stocks.getStatename()).setStatecode(stocks.getStatecode())
                .setSellerid(stocks.getSellerid()).setPri(stocks.getPri()).setName(stocks.getName())
                .setLat(stocks.getLat()).setLng(stocks.getLng()).build();
        responseObserver.onNext(stockEntity);
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(StockRequest request, StreamObserver<StockListResponse> responseObserver) {
        Stocks stockDto=new Stocks();
        stockDto.setName(request.getName());
        stockDto.setCountyname(request.getCountyname());
        stockDto.setCountycode(request.getCountycode());
        stockDto.setCountrycode(request.getCountrycode());
        stockDto.setCountry(request.getCountry());
        stockDto.setStatename(request.getStatename());
        stockDto.setStatecode(request.getStatecode());
        stockDto.setSellerid(request.getSellerid());
        stockDto.setCityname(request.getCityname());
        stockDto.setCitycode(request.getCitycode());

        List<Stocks> stocksList= stocksMapper.findByCondition(stockDto);
        StockListResponse.Builder builder=StockListResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功");
        for(int i=0;i<stocksList.size();i++){
            Stocks stocks=stocksList.get(i);
            StockEntity stockEntity= StockEntity.newBuilder().setAddress(stocks.getAddress())
                    .setCitycode(stocks.getCitycode()).setCityname(stocks.getCityname())
                    .setCountry(stocks.getCountry()).setCountrycode(stocks.getCountrycode())
                    .setCountycode(stocks.getCountycode()).setCountyname(stocks.getCountyname())
                    .setId(stocks.getId()).setZipcode(stocks.getZipcode())
                    .setStatename(stocks.getStatename()).setStatecode(stocks.getStatecode())
                    .setSellerid(stocks.getSellerid()).setPri(stocks.getPri()).setName(stocks.getName())
                    .setLat(stocks.getLat()).setLng(stocks.getLng()).build();
            builder.addStockEntity(stockEntity);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void findPage(StockRequest request, StreamObserver<StockPageResponse> responseObserver) {
        Stocks stockDto=new Stocks();
        stockDto.setName(request.getName());
        stockDto.setCountyname(request.getCountyname());
        stockDto.setCountycode(request.getCountycode());
        stockDto.setCountrycode(request.getCountrycode());
        stockDto.setCountry(request.getCountry());
        stockDto.setStatename(request.getStatename());
        stockDto.setStatecode(request.getStatecode());
        stockDto.setSellerid(request.getSellerid()==0?null:request.getSellerid());
        stockDto.setCityname(request.getCityname());
        stockDto.setCitycode(request.getCitycode());

        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        PageInfo<Stocks> pageInfo = new PageInfo<Stocks>(stocksMapper.findByCondition(stockDto));
        List<Stocks>stocksList=pageInfo.getList() ;
        Long total=pageInfo.getTotal();
        StockPageResponse.Builder builder=StockPageResponse.newBuilder().setIsSuccess(true)
                .setMsg("查询成功").setCurrentPage(request.getCurrentPage())
                .setPageSize(request.getPageSize()).setTotal(total);
        for(int i=0;i<stocksList.size();i++){
            Stocks stocks=stocksList.get(i);
            StockEntity stockEntity= StockEntity.newBuilder().setAddress(stocks.getAddress())
                    .setCitycode(stocks.getCitycode()).setCityname(stocks.getCityname())
                    .setCountry(stocks.getCountry()).setCountrycode(stocks.getCountrycode())
                    .setCountycode(stocks.getCountycode()).setCountyname(stocks.getCountyname())
                    .setId(stocks.getId()).setZipcode(stocks.getZipcode())
                    .setStatename(stocks.getStatename()).setStatecode(stocks.getStatecode())
                    .setSellerid(stocks.getSellerid()).setPri(stocks.getPri()).setName(stocks.getName())
                    .setLat(stocks.getLat()).setLng(stocks.getLng()).build();

            builder.addData(stockEntity);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
