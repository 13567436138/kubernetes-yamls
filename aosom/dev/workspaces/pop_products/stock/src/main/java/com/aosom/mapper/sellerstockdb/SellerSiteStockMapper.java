package com.aosom.mapper.sellerstockdb;

import com.aosom.model.SellerSiteStock;
import com.aosom.service.stock.Sellersitestock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SellerSiteStockMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SellerSiteStock record);

    SellerSiteStock selectByPrimaryKey(Long id);

    List<SellerSiteStock> selectAll();

    int updateByPrimaryKey(SellerSiteStock record);

    /**
     * 根据siteid和sellerid查询优先级从高到低排序的商家站点发货仓库
     * @param siteid
     * @param sellerId
     * @return
     */
    List<SellerSiteStock> findBySiteidAndSelleridOrdered(@Param("siteid") int siteid,@Param("sellerid") Long sellerid);

    /**
     * 按条件查询
     * @param sellerSiteStock
     * @return
     */
    List<SellerSiteStock> findByCondition(SellerSiteStock sellerSiteStock);
}