package com.aosom.exception;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/25 09:18
 * @Description:
 */
public class DoNotHaveEnoughSkuStockException extends RuntimeException {
    public DoNotHaveEnoughSkuStockException(String msg){
        super(msg);
    }
}
