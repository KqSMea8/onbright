package com.bright.apollo.common.entity;

import org.springframework.util.StringUtils;

import java.util.Date;

public class TYaokonyunRemoteControl {

    public TYaokonyunRemoteControl(MatchRemoteControl matchRemoteControl) {
        this.r_id = matchRemoteControl.getRid();
        this.t_id = matchRemoteControl.gettId();
        this.version = matchRemoteControl.getVersion();
        this.name = matchRemoteControl.getName();
        this.beRmodel = matchRemoteControl.getBeRmodel();
        this.rmodel = matchRemoteControl.getRmodel();
        this.rdesc = matchRemoteControl.getRdesc();
        this.order_no = matchRemoteControl.getOrderNo();
        this.zip = matchRemoteControl.getZip();
        if(!StringUtils.isEmpty(matchRemoteControl.getZero())){
            this.zero=matchRemoteControl.getZero();
        }
        if(!StringUtils.isEmpty(matchRemoteControl.getOne())){
            this.one=matchRemoteControl.getOne();
        }
    }

    public TYaokonyunRemoteControl() {
    }

    private Integer id;

    private String r_id;

    private Integer t_id;

    private Integer version;

    private String name;

    private String beRmodel;

    private String rmodel;

    private String rdesc;

    private String order_no;

    private Integer zip;

    private Date lastOpTime;

    private String one;

    private String zero;

    private Integer num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public Integer getT_id() {
        return t_id;
    }

    public void setT_id(Integer t_id) {
        this.t_id = t_id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeRmodel() {
        return beRmodel;
    }

    public void setBeRmodel(String beRmodel) {
        this.beRmodel = beRmodel;
    }

    public String getRmodel() {
        return rmodel;
    }

    public void setRmodel(String rmodel) {
        this.rmodel = rmodel;
    }

    public String getRdesc() {
        return rdesc;
    }

    public void setRdesc(String rdesc) {
        this.rdesc = rdesc;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public Date getLastOpTime() {
        return lastOpTime;
    }

    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getZero() {
        return zero;
    }

    public void setZero(String zero) {
        this.zero = zero;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
