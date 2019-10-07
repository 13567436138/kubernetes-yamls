package com.aosom.aosombase.entity;

public class BaseResponse<T> {

    private static final int SUCCESS_CODE = 0;

    private static final int UNKNOWN_EXCEPTION = -99;


    public BaseResponse(){
        this.code=SUCCESS_CODE;
    }
    /**
     * 返回的信息(主要出错的时候使用)
     */
    private String msg;
    /**
     * 接口返回码
     */
    private int code;
    /**
     * 返回的数据
     */
    private T data;
    /**
     * 返回的信息code(通过这个code从语言资源文件中查找错误提示信息)
     */
    private String msgCode;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
