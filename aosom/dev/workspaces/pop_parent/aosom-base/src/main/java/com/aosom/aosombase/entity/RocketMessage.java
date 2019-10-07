package com.aosom.aosombase.entity;

/**
 * 消息发送实体
 */
public class RocketMessage {

        private  String messageType;    //消息类型

        private  String   data;//  json  类型

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
