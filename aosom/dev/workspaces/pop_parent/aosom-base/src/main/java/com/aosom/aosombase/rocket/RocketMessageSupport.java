package com.aosom.aosombase.rocket;

import com.aosom.aosombase.entity.RocketMessage;

import java.util.List;

/**
 *解析消息
 */
public interface RocketMessageSupport {

    /**
     * 是否支持消息
     * @param message
     * @return
     */
    boolean   support(RocketMessage  message);

    /**
     * 填写支持的消息类型
     * @return
     */
    String  supportType();

    /**
     * 获取data
     * @param type
     * @param <T>
     * @return
     */
     <T>   T  getData(Class<T>  type);
     <T> List<T> getDataList(Class<T>  type);

    /**
     * 处理逻辑
     */
    void   process();
}
