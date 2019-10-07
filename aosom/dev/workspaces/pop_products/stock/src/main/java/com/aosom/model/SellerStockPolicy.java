package com.aosom.model;

import java.util.Date;

public class SellerStockPolicy {
    private Long id;

    private Long sellerid;

    private Integer siteid;

    private String countrycode;

    private String statecode;

    private String countycode;

    private String stockidlist;

    private Date ct;

    private Date ut;

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

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode == null ? null : countrycode.trim();
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode == null ? null : statecode.trim();
    }

    public String getCountycode() {
        return countycode;
    }

    public void setCountycode(String countycode) {
        this.countycode = countycode == null ? null : countycode.trim();
    }

    public String getStockidlist() {
        return stockidlist;
    }

    public void setStockidlist(String stockidlist) {
        this.stockidlist = stockidlist == null ? null : stockidlist.trim();
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