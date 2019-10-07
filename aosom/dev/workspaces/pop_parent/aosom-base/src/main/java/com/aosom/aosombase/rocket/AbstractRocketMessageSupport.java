package com.aosom.aosombase.rocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aosom.aosombase.entity.RocketMessage;
import com.google.gson.annotations.JsonAdapter;
import org.springframework.util.StringUtils;

import java.util.List;

public  abstract class AbstractRocketMessageSupport  implements   RocketMessageSupport {
     private RocketMessage  rocketMessage;

    @Override
    public boolean support(RocketMessage message) {
        if(supportType().equals(message.getMessageType())){
            rocketMessage=message;
            return true;
        }
        return false;
    }
    @Override
    public <T> T getData(Class<T> type) {
       if(rocketMessage!=null&&!StringUtils.isEmpty(rocketMessage.getData())){
           return   JSON.parseObject(rocketMessage.getData(),type);
       }
        return null;
    }

    @Override
    public <T> List<T> getDataList(Class<T> type) {
        if(rocketMessage!=null&&!StringUtils.isEmpty(rocketMessage.getData())){
            return   JSON.parseArray(rocketMessage.getData(),type);
        }
        return null;
    }
}
