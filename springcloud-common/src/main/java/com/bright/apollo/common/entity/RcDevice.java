package com.bright.apollo.common.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RcDevice {

    private static final long serialVersionUID = -3333l;

    @Expose(serialize = false)
    protected Integer index;//本地数据库维护
    /**
     * 遥控器ID
     */
    @Expose
    protected String rid;

    /**
     * 遥控设备厂牌名称
     */
    @Expose
    protected String  name;

    /**
     * 遥控设备类型
     */
    @Expose
    @SerializedName("t")
    protected Integer  tId;

    /**
     * 被遥控设备型号
     */
    @Expose(serialize = false)
    @SerializedName("be_rmodel")
    protected String  beRmodel;

    /**
     * 遥控器型号
     */
    @Expose(serialize = false)
    protected String  rmodel;

    /**
     * 自定义设备描述
     */
    @Expose(serialize = false)
    protected String  rdesc;

    /**
     * 排序
     */
    @Expose(serialize = false)
    @SerializedName("order_no")
    protected String  orderNo;

    /**
     * 是否压缩
     */
    @Expose(serialize = false)
    protected Integer  zip;


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCodeset() {
        return codeset;
    }

    public void setCodeset(Integer codeset) {
        this.codeset = codeset;
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

    /**
     * 遥控码版本
     */
    @Expose
    @SerializedName("v")
    protected Integer version;

    /**
     * 码库ID
     */
    @Expose(serialize = false)
    protected Integer codeset;

    /**
     * 1代表码值
     */
    @Expose(serialize = false)
    @SerializedName("_o")
    protected String one;

    /**
     * 0代表码值
     */
    @Expose(serialize = false)
    @SerializedName("_z")
    protected String zero;
}
