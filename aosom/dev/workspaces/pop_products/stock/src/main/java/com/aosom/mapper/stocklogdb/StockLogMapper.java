package com.aosom.mapper.stocklogdb;

import com.aosom.model.StockLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface StockLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StockLog record);

    StockLog selectByPrimaryKey(Long id);

    List<StockLog> selectAll();

    int updateByPrimaryKey(StockLog record);

    /**
     * 根据条件查询
     * @param stockLog
     * @return
     */
    List<StockLog> findByCondition(StockLog stockLog);
}