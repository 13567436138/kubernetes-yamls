package com.aosom.mapper.sellerstockdb;

import com.aosom.model.SellerSite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SellerSiteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SellerSite record);

    SellerSite selectByPrimaryKey(Long id);

    List<SellerSite> selectAll();

    int updateByPrimaryKey(SellerSite record);

    /**
     * 根据siteid和sellerid获取单条记录
     * @param siteid
     * @param sellerid
     * @return
     */
    SellerSite getBySiteidAndSellerid(@Param("siteid")int siteid,@Param("sellerid")Long sellerid);

    /**
     * 按条件查询
     * @param sellerSite
     * @return
     */
    List<SellerSite> findByCondition(SellerSite sellerSite);
}