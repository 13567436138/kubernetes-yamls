package com.aosom.exception;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/25 08:57
 * @Description:
 */
public class SellerSiteStockNotFoundException extends RuntimeException {
    public SellerSiteStockNotFoundException(String msg){
        super(msg);
    }
}
