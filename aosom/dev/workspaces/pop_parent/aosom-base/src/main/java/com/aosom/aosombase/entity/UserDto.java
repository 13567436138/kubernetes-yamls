package com.aosom.aosombase.entity;



import java.util.Date;


public class UserDto {
    private Long uid;

    private String mobile;

    private String mobileRegion;

    private String email;

    private Byte emailverify;

    private String pwd;

    private String nickname;

    private String firstname;

    private String lastname;

    private Byte gender;

    private Date birthday;

    private Byte secureLevel;

    private Integer registerSiteid;

    private Byte registerType;

    private Byte snstype;

    private Byte status;

    private Date freezeEt;

    private Long inviteuid;

    private String utmsrc;

    private String apptype;

    private Byte pubtype;

    private Byte clienttype;

    private String clientip;

    private Date lasttime;

    private Date ct;

    private Date ut;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileRegion() {
        return mobileRegion;
    }

    public void setMobileRegion(String mobileRegion) {
        this.mobileRegion = mobileRegion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getEmailverify() {
        return emailverify;
    }

    public void setEmailverify(Byte emailverify) {
        this.emailverify = emailverify;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Byte getSecureLevel() {
        return secureLevel;
    }

    public void setSecureLevel(Byte secureLevel) {
        this.secureLevel = secureLevel;
    }

    public Integer getRegisterSiteid() {
        return registerSiteid;
    }

    public void setRegisterSiteid(Integer registerSiteid) {
        this.registerSiteid = registerSiteid;
    }

    public Byte getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Byte registerType) {
        this.registerType = registerType;
    }

    public Byte getSnstype() {
        return snstype;
    }

    public void setSnstype(Byte snstype) {
        this.snstype = snstype;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getFreezeEt() {
        return freezeEt;
    }

    public void setFreezeEt(Date freezeEt) {
        this.freezeEt = freezeEt;
    }

    public Long getInviteuid() {
        return inviteuid;
    }

    public void setInviteuid(Long inviteuid) {
        this.inviteuid = inviteuid;
    }

    public String getUtmsrc() {
        return utmsrc;
    }

    public void setUtmsrc(String utmsrc) {
        this.utmsrc = utmsrc;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public Byte getPubtype() {
        return pubtype;
    }

    public void setPubtype(Byte pubtype) {
        this.pubtype = pubtype;
    }

    public Byte getClienttype() {
        return clienttype;
    }

    public void setClienttype(Byte clienttype) {
        this.clienttype = clienttype;
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
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
