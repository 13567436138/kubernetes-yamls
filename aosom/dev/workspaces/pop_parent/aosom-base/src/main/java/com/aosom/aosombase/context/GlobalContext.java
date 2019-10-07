package com.aosom.aosombase.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局存储
 */
public class GlobalContext {

    private  GlobalContext(){

    }

    private static    GlobalContext  context;
    public  static  final   String     SELLER_ID="SELLER_ID";   //商户id
    public  static  final  String    MEMBER_ID="MEMBER_ID";    //前台用户id
    private  static  Object lock=new Object();   //全局锁

    /**
     * 保存
     */
    private ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<Map<String, String>>() {
        protected Map<String, String> initialValue() {
            return new HashMap();
        }
    };
    private final  static     GlobalContext crateContext(){
        if(context==null){
            synchronized(lock) {
                if(context==null){
                    context=new GlobalContext();
                }
            }
        }
        return context;
    }

    /**
     * 获取商户id
     * @return
     */
    public  static  String  getSellerId(){
         return   crateContext().threadLocal.get().get(SELLER_ID);
    }



    /**
     * 获取memberId
     * @return
     */
    public  static  String getMemberId(){
       return crateContext().threadLocal.get().get(SELLER_ID);
    }

    /**
     * 根据key  来获取值
     * @param key
     * @return
     */
    public static  String  getKey(String key){
        return crateContext().threadLocal.get().get(key);
    }

    /**
     * 绑定全局值
     * @param key
     * @param value
     */
    public  static  void   saveKey(String key,String value){
        crateContext().threadLocal.get().put(key,value);
    }

    /**
     * 移除key
     * @param key
     */
    public static  void removeKey(String key){
        crateContext().threadLocal.get().remove(key);
    }

    /**
     * 移除全局key
     */
    public  static  void removeAllKey(){
        crateContext().threadLocal.remove();
    }


}
