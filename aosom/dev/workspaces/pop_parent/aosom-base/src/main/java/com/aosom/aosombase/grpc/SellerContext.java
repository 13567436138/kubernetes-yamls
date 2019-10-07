package com.aosom.aosombase.grpc;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/27 08:25
 * @Description:
 */
public class SellerContext {
    private static ThreadLocal<Long> selleridLocal=new ThreadLocal<>();
    public static final String SELLER_ID_KEY="seller_id";

    public static Long getSellerId(){
        return selleridLocal.get();
    }

    public static void setSellerId(Long sellerId){
        selleridLocal.set(sellerId);
    }
    public static void unbind() {
         selleridLocal.remove();
    }

}
