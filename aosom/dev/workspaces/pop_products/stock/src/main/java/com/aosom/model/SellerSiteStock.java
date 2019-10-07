package com.aosom.model;

public class SellerSiteStock {
    private Long id;

    private Long sellerid;

    private Integer siteid;

    private Long stockid;

    private Byte pri;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerid() {
        return sellerid;
    }

    public void setSellerid(Long sellerid) {
        this.sellerid = sellerid;
    }

    public Integer getSiteid() {
        return siteid;
    }

    public void setSiteid(Integer siteid) {
        this.siteid = siteid;
    }

    public Long getStockid() {
        return stockid;
    }

    public void setStockid(Long stockid) {
        this.stockid = stockid;
    }

    public Byte getPri() {
        return pri;
    }

    public void setPri(Byte pri) {
        this.pri = pri;
    }
}