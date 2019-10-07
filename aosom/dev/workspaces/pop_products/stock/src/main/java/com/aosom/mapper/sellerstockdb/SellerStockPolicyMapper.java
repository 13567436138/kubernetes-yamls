package com.aosom.mapper.sellerstockdb;

import com.aosom.model.SellerStockPolicy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SellerStockPolicyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SellerStockPolicy record);

    SellerStockPolicy selectByPrimaryKey(Long id);

    List<SellerStockPolicy> selectAll();

    int updateByPrimaryKey(SellerStockPolicy record);

    /**
     * 获取调度策略
     * @param sellerid
     * @param siteid
     * @param countrycode
     * @param statecode
     * @param countycode
     * @return
     */
    SellerStockPolicy getBySelleridAndSiteidAndCountrycodeAndStateCodeAndCountyCode(@Param("sellerid")Long sellerid,@Param("siteid") int siteid,@Param("countrycode") String countrycode,@Param("statecode") String statecode,@Param("countycode") String countycode);

    /**
     * 按条件查询
     * @param sellerStockPolicy
     * @return
     */
    List<SellerStockPolicy> findByCondition(SellerStockPolicy sellerStockPolicy);
}