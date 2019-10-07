package com.aosom.model;

import java.util.Date;

public class StockLog {
    private Long id;

    private Long uid;

    private Long orderid;

    private Integer siteid;

    private Byte from;

    private Byte optype;

    private String skustockinfo;

    private Date ct;

    private Date ut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public Integer getSiteid() {
        return siteid;
    }

    public void setSiteid(Integer siteid) {
        this.siteid = siteid;
    }

    public Byte getFrom() {
        return from;
    }

    public void setFrom(Byte from) {
        this.from = from;
    }

    public Byte getOptype() {
        return optype;
    }

    public void setOptype(Byte optype) {
        this.optype = optype;
    }

    public String getSkustockinfo() {
        return skustockinfo;
    }

    public void setSkustockinfo(String skustockinfo) {
        this.skustockinfo = skustockinfo == null ? null : skustockinfo.trim();
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

    public Date getUt() {
        return ut;
    }

    public void setUt(Date ut) {
        this.ut = ut;
    }
}