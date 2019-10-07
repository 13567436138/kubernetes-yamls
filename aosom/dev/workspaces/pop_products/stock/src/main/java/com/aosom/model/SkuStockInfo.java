package com.aosom.model;

import java.util.Date;

public class SkuStockInfo {
    private Long id;

    private Long skuid;

    private Long stockid;

    private Long sellerid;

    private Integer realnum;

    private Integer locknum;

    private Date ut;

    private Date ct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuid() {
        return skuid;
    }

    public void setSkuid(Long skuid) {
        this.skuid = skuid;
    }

    public Long getStockid() {
        return stockid;
    }

    public void setStockid(Long stockid) {
        this.stockid = stockid;
    }

    public Long getSellerid() {
        return sellerid;
    }

    public void setSellerid(Long sellerid) {
        this.sellerid = sellerid;
    }

    public Integer getRealnum() {
        return realnum;
    }

    public void setRealnum(Integer realnum) {
        this.realnum = realnum;
    }

    public Integer getLocknum() {
        return locknum;
    }

    public void setLocknum(Integer locknum) {
        this.locknum = locknum;
    }

    public Date getUt() {
        return ut;
    }

    public void setUt(Date ut) {
        this.ut = ut;
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }
}