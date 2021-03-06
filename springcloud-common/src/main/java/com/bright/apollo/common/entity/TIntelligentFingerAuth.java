package com.bright.apollo.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TIntelligentFingerAuth implements Serializable{
    /**  
	 *   
	 */
	private static final long serialVersionUID = 209675273649928989L;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_auth.id
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_auth.serialId
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private String serialid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_auth.pwd
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private String pwd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_intelligent_finger_auth.last_op_time
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    private Date lastOpTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_auth.id
     *
     * @return the value of t_intelligent_finger_auth.id
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_auth.id
     *
     * @param id the value for t_intelligent_finger_auth.id
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_auth.serialId
     *
     * @return the value of t_intelligent_finger_auth.serialId
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public String getSerialid() {
        return serialid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_auth.serialId
     *
     * @param serialid the value for t_intelligent_finger_auth.serialId
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setSerialid(String serialid) {
        this.serialid = serialid == null ? null : serialid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_auth.pwd
     *
     * @return the value of t_intelligent_finger_auth.pwd
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_auth.pwd
     *
     * @param pwd the value for t_intelligent_finger_auth.pwd
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_intelligent_finger_auth.last_op_time
     *
     * @return the value of t_intelligent_finger_auth.last_op_time
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public Date getLastOpTime() {
        return lastOpTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_intelligent_finger_auth.last_op_time
     *
     * @param lastOpTime the value for t_intelligent_finger_auth.last_op_time
     *
     * @mbg.generated Mon Aug 06 10:56:24 CST 2018
     */
    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }
}