package com.bright.apollo.common.entity;

import java.io.Serializable;
import java.util.Date;

public class TYaokonyunKeyCode implements Serializable {

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

    private Integer remoteId;

    private Integer brandId;

    private Integer tId;

    private String name;

    private String rmodel;

    private Integer version;

    private Integer keyType;

    public Integer getKeyType() {
        return keyType;
    }

    public void setKeyType(Integer keyType) {
        this.keyType = keyType;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRmodel() {
        return rmodel;
    }

    public void setRmodel(String rmodel) {
        this.rmodel = rmodel;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

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
