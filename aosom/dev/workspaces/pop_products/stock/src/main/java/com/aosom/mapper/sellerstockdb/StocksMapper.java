package com.aosom.mapper.sellerstockdb;

import com.aosom.model.Stocks;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * author:hxp
 */
@Mapper
@Component
public interface StocksMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stocks record);

    Stocks selectByPrimaryKey(Long id);

    List<Stocks> selectAll();

    int updateByPrimaryKey(Stocks record);

    /**
     * 根据距离查询存在sku的仓库，按距离远近排序
     * @param
     * @return
     */
    List<Stocks> findSellerStocks(Long sellerid);

    /**
     * 按条件查询
     * @param stocks
     * @return
     */
    List<Stocks> findByCondition(Stocks stocks);
}