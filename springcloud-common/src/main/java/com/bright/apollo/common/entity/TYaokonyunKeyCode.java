package com.bright.apollo.common.entity;

import java.util.Date;

public class TYaokonyunKeyCode {

    private Integer id;//remark:obox name;length:128; not null,default:null

    private Integer index;//remark:obox name;length:128; not null,default:null

    private String key;//remark:obox name;length:128; not null,default:null

    private String kn;//remark:obox name;length:128; not null,default:null

    private String analysisSrc;//remark:obox name;length:128; not null,default:null

    private String src;//remark:obox name;length:128; not null,default:null

    private String shortsrc;//remark:obox name;length:128; not null,default:null

    private Integer order;//remark:obox name;length:128; not null,default:null

    private Date lastOpTime;

    private String serialId;

    private String keyName;

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    private String customName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKn() {
        return kn;
    }

    public void setKn(String kn) {
        this.kn = kn;
    }

    public String getAnalysisSrc() {
        return analysisSrc;
    }

    public void setAnalysisSrc(String analysisSrc) {
        this.analysisSrc = analysisSrc;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getShortsrc() {
        return shortsrc;
    }

    public void setShortsrc(String shortsrc) {
        this.shortsrc = shortsrc;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getLastOpTime() {
        return lastOpTime;
    }

    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
