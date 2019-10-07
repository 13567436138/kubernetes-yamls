package com.aosom.model;

import java.util.Date;

public class SkuSafeNum {
    private Long id;

    private Integer siteid;

    private Long skuid;

    private Integer safenum;

    private Date ct;

    private Date ut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSiteid() {
        return siteid;
    }

    public void setSiteid(Integer siteid) {
        this.siteid = siteid;
    }

    public Long getSkuid() {
        return skuid;
    }

    public void setSkuid(Long skuid) {
        this.skuid = skuid;
    }

    public Integer getSafenum() {
        return safenum;
    }

    public void setSafenum(Integer safenum) {
        this.safenum = safenum;
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