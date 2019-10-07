package com.aosom.dto;

/**
 * @Auther: 黄小平
 * @Date: 2019/9/25 15:36
 * @Description:
 */
public class SkuSafeNumDto {
    private Integer siteid;
    private Integer safenumStart;
    private Integer safenumEnd;
    private Long skuid;
    private Long  id;

    public Integer getSiteid() {
        return siteid;
    }

    public void setSiteid(Integer siteid) {
        this.siteid = siteid;
    }

    public Integer getSafenumStart() {
        return safenumStart;
    }

    public void setSafenumStart(Integer safenumStart) {
        this.safenumStart = safenumStart;
    }

    public Integer getSafenumEnd() {
        return safenumEnd;
    }

    public void setSafenumEnd(Integer safenumEnd) {
        this.safenumEnd = safenumEnd;
    }

    public Long getSkuid() {
        return skuid;
    }

    public void setSkuid(Long skuid) {
        this.skuid = skuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
