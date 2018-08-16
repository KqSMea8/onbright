package com.bright.apollo.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TIntelligentFingerPush implements Serializable{
    /**  
	 *   
	 */
	private static final long serialVersionUID = -83224597548399758L;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_push.id
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_push.serialId
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private String serialid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_push.mobile
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private String mobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_push.cmd
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private String cmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_push.value
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private Integer value;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_push.enable
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private Integer enable;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_push.last_op_time
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private Date lastOpTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_push.id
     *
     * @return the value of t_intelligent_finger_push.id
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_push.id
     *
     * @param id the value for t_intelligent_finger_push.id
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_push.serialId
     *
     * @return the value of t_intelligent_finger_push.serialId
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public String getSerialid() {
        return serialid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_push.serialId
     *
     * @param serialid the value for t_intelligent_finger_push.serialId
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setSerialid(String serialid) {
        this.serialid = serialid == null ? null : serialid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_push.mobile
     *
     * @return the value of t_intelligent_finger_push.mobile
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_push.mobile
     *
     * @param mobile the value for t_intelligent_finger_push.mobile
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_push.cmd
     *
     * @return the value of t_intelligent_finger_push.cmd
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_push.cmd
     *
     * @param cmd the value for t_intelligent_finger_push.cmd
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setCmd(String cmd) {
        this.cmd = cmd == null ? null : cmd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_push.value
     *
     * @return the value of t_intelligent_finger_push.value
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public Integer getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_push.value
     *
     * @param value the value for t_intelligent_finger_push.value
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_push.enable
     *
     * @return the value of t_intelligent_finger_push.enable
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_push.enable
     *
     * @param enable the value for t_intelligent_finger_push.enable
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_push.last_op_time
     *
     * @return the value of t_intelligent_finger_push.last_op_time
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public Date getLastOpTime() {
        return lastOpTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_push.last_op_time
     *
     * @param lastOpTime the value for t_intelligent_finger_push.last_op_time
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }
}