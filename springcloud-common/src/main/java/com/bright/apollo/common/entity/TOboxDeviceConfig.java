package com.bright.apollo.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value={"id","lastOpTime"})
public class TOboxDeviceConfig implements Serializable{
    /**  
	 *   
	 */
	private static final long serialVersionUID = 1523535654261457763L;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.obox_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="obox_id")
    private Integer oboxId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.device_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="name")
    private String deviceId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.device_state
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="state")
    private String deviceState;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.device_type
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="device_type")
    private String deviceType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.device_child_type
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="device_child_type")
    private String deviceChildType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.device_version
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="version")
    private String deviceVersion;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.last_op_time
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    private Date lastOpTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.device_serial_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="serialId")
    private String deviceSerialId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.device_rf_addr
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="addr")
    private String deviceRfAddr;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.group_addr
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="group_addr")
    private String groupAddr;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.obox_serial_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="obox_serial_id")
    private String oboxSerialId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_obox_device_config.online
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    @JsonProperty(value="online")
    private Integer online;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.id
     *
     * @return the value of t_obox_device_config.id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.id
     *
     * @param id the value for t_obox_device_config.id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.obox_id
     *
     * @return the value of t_obox_device_config.obox_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public Integer getOboxId() {
        return oboxId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.obox_id
     *
     * @param oboxId the value for t_obox_device_config.obox_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setOboxId(Integer oboxId) {
        this.oboxId = oboxId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.device_id
     *
     * @return the value of t_obox_device_config.device_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.device_id
     *
     * @param deviceId the value for t_obox_device_config.device_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.device_state
     *
     * @return the value of t_obox_device_config.device_state
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getDeviceState() {
        return deviceState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.device_state
     *
     * @param deviceState the value for t_obox_device_config.device_state
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState == null ? null : deviceState.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.device_type
     *
     * @return the value of t_obox_device_config.device_type
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.device_type
     *
     * @param deviceType the value for t_obox_device_config.device_type
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.device_child_type
     *
     * @return the value of t_obox_device_config.device_child_type
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getDeviceChildType() {
        return deviceChildType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.device_child_type
     *
     * @param deviceChildType the value for t_obox_device_config.device_child_type
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setDeviceChildType(String deviceChildType) {
        this.deviceChildType = deviceChildType == null ? null : deviceChildType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.device_version
     *
     * @return the value of t_obox_device_config.device_version
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getDeviceVersion() {
        return deviceVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.device_version
     *
     * @param deviceVersion the value for t_obox_device_config.device_version
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion == null ? null : deviceVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.last_op_time
     *
     * @return the value of t_obox_device_config.last_op_time
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public Date getLastOpTime() {
        return lastOpTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.last_op_time
     *
     * @param lastOpTime the value for t_obox_device_config.last_op_time
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.device_serial_id
     *
     * @return the value of t_obox_device_config.device_serial_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getDeviceSerialId() {
        return deviceSerialId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.device_serial_id
     *
     * @param deviceSerialId the value for t_obox_device_config.device_serial_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setDeviceSerialId(String deviceSerialId) {
        this.deviceSerialId = deviceSerialId == null ? null : deviceSerialId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.device_rf_addr
     *
     * @return the value of t_obox_device_config.device_rf_addr
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getDeviceRfAddr() {
        return deviceRfAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.device_rf_addr
     *
     * @param deviceRfAddr the value for t_obox_device_config.device_rf_addr
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setDeviceRfAddr(String deviceRfAddr) {
        this.deviceRfAddr = deviceRfAddr == null ? null : deviceRfAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.group_addr
     *
     * @return the value of t_obox_device_config.group_addr
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getGroupAddr() {
        return groupAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.group_addr
     *
     * @param groupAddr the value for t_obox_device_config.group_addr
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setGroupAddr(String groupAddr) {
        this.groupAddr = groupAddr == null ? null : groupAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.obox_serial_id
     *
     * @return the value of t_obox_device_config.obox_serial_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public String getOboxSerialId() {
        return oboxSerialId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.obox_serial_id
     *
     * @param oboxSerialId the value for t_obox_device_config.obox_serial_id
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setOboxSerialId(String oboxSerialId) {
        this.oboxSerialId = oboxSerialId == null ? null : oboxSerialId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_obox_device_config.online
     *
     * @return the value of t_obox_device_config.online
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public Integer getOnline() {
        return online;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_obox_device_config.online
     *
     * @param online the value for t_obox_device_config.online
     *
     * @mbg.generated Wed Mar 14 16:07:21 CST 2018
     */
    public void setOnline(Integer online) {
        this.online = online;
    }
}