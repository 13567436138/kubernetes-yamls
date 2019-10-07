package com.aosom.mapper.skustockdb;

import com.aosom.model.SkuLockInfo;
import com.aosom.service.stock.Skulockinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SkuLockInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SkuLockInfo record);

    SkuLockInfo selectByPrimaryKey(Long id);

    List<SkuLockInfo> selectAll();

    int updateByPrimaryKey(SkuLockInfo record);

    /**
     * 根据订单id查询锁定信息
     * @param orderId
     * @return
     */
    List<SkuLockInfo> findByOrderId(Long orderId);

    /**
     * 查询每个sku的锁定数量
     * @param orderId
     * @return
     */
    List<SkuLockInfo> findByOrderIdGroupBySkuid(Long orderId);

    /**
     * 查询
     * @param orderid
     * @param sellerid
     * @param skuid
     * @param stockid
     * @return
     */
    SkuLockInfo getByOrderidAndSellerIdAndSkuidAndStockId(@Param("orderid")Long orderid,@Param("sellerid") Long sellerid,@Param("skuid") Long skuid, @Param("stockid")Long stockid);

    /**
     * 更新锁定数量
     * @param skuLockInfo
     */
    void updateLockNumById(SkuLockInfo skuLockInfo);
}