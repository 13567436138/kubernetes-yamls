package com.aosom.mapper.skustockdb;

import com.aosom.model.SkuStockInfo;
import com.aosom.model.Stocks;
import com.aosom.service.stock.Skustockinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SkuStockInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SkuStockInfo record);

    SkuStockInfo selectByPrimaryKey(Long id);

    List<SkuStockInfo> selectAll();

    int updateByPrimaryKey(SkuStockInfo record);

    /**
     * 根据sellerid，skuid，stockid获取库存信息
     * @param sellerid
     * @param skuid
     * @param stockid
     * @return
     */
    SkuStockInfo getBySeleridAndSkuidAndStockid(@Param("sellerid") Long sellerid, @Param("skuid")Long skuid, @Param("stockid")Long stockid);

    /**
     * 检查是否有足够库存
     * @param stockIds
     * @param skuid
     * @param sellerId
     * @param num
     * @return
     */
    boolean checkIfHaveEnoughSkuStockInfo(@Param("stockIds") List<Long> stockIds,@Param("skuid")Long skuid,@Param("sellerid")Long sellerid,@Param("num")int num);

    /**
     * 解锁
     * @param sellerid
     * @param skuid
     * @param stockid
     * @param locknum
     */
    void unLockNum(@Param("sellerid")Long sellerid, @Param("skuid")Long skuid,@Param("stockid") Long stockid,@Param("locknum")int locknum);

    /**
     * 减库存
     * @param sellerid
     * @param skuid
     * @param stockid
     * @param locknum
     */
    void deduct(@Param("sellerid")Long sellerid, @Param("skuid")Long skuid, @Param("stockid")Long stockid,@Param("locknum")int locknum);

    /**
     * 回退
     * @param sellerid
     * @param skuid
     * @param stockid
     * @param restorenum
     */
    void restore(@Param("sellerid") Long sellerid, @Param("skuid")Long skuid,@Param("stockid") Long stockid,@Param("restorenum")int restorenum);

    /**
     * 查询仓库id
     * @param skuid
     * @param sellerid
     * @param stockIds
     * @return
     */
    List<Long> findBySkuIdAndSelleridAndStockIds(@Param("skuid")Long skuid,@Param("sellerid")Long sellerid,@Param("stockIds")List<Long>  stockIds);

    /**
     * 查询
     * @param skuStockInfo
     * @return
     */
    List<SkuStockInfo> findByCondition(SkuStockInfo skuStockInfo);
}