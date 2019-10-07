package com.aosom.mapper.skustockdb;

import com.aosom.dto.SkuSafeNumDto;
import com.aosom.model.SkuSafeNum;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SkuSafeNumMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SkuSafeNum record);

    SkuSafeNum selectByPrimaryKey(Long id);

    List<SkuSafeNum> selectAll();

    int updateByPrimaryKey(SkuSafeNum record);

    /**
     * 查询安全库存
     * @param skuSafeNumDto
     * @return
     */
    List<SkuSafeNum> findByCondition(SkuSafeNumDto skuSafeNumDto);
}