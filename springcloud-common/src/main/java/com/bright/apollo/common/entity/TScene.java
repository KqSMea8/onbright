package com.bright.apollo.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TScene implements Serializable{
    /**  
	 *   
	 */
	private static final long serialVersionUID = 7829168832002132824L;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.scene_number
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private Integer sceneNumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.scene_name
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private String sceneName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.obox_serial_id
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private String oboxSerialId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.obox_scene_number
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private Integer oboxSceneNumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.scene_status
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private Integer sceneStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.scene_type
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private String sceneType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.msg_alter
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private Byte msgAlter;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.last_op_time
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private Date lastOpTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.scene_run
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private Integer sceneRun;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.license
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private Integer license;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.alter_need
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private Integer alterNeed;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_scene.scene_group
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    private String sceneGroup;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.scene_number
     *
     * @return the value of t_scene.scene_number
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public Integer getSceneNumber() {
        return sceneNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.scene_number
     *
     * @param sceneNumber the value for t_scene.scene_number
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setSceneNumber(Integer sceneNumber) {
        this.sceneNumber = sceneNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.scene_name
     *
     * @return the value of t_scene.scene_name
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public String getSceneName() {
        return sceneName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.scene_name
     *
     * @param sceneName the value for t_scene.scene_name
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setSceneName(String sceneName) {
        this.sceneName = sceneName == null ? null : sceneName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.obox_serial_id
     *
     * @return the value of t_scene.obox_serial_id
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public String getOboxSerialId() {
        return oboxSerialId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.obox_serial_id
     *
     * @param oboxSerialId the value for t_scene.obox_serial_id
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setOboxSerialId(String oboxSerialId) {
        this.oboxSerialId = oboxSerialId == null ? null : oboxSerialId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.obox_scene_number
     *
     * @return the value of t_scene.obox_scene_number
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public Integer getOboxSceneNumber() {
        return oboxSceneNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.obox_scene_number
     *
     * @param oboxSceneNumber the value for t_scene.obox_scene_number
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setOboxSceneNumber(Integer oboxSceneNumber) {
        this.oboxSceneNumber = oboxSceneNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.scene_status
     *
     * @return the value of t_scene.scene_status
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public Integer getSceneStatus() {
        return sceneStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.scene_status
     *
     * @param sceneStatus the value for t_scene.scene_status
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setSceneStatus(Integer sceneStatus) {
        this.sceneStatus = sceneStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.scene_type
     *
     * @return the value of t_scene.scene_type
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public String getSceneType() {
        return sceneType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.scene_type
     *
     * @param sceneType the value for t_scene.scene_type
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setSceneType(String sceneType) {
        this.sceneType = sceneType == null ? null : sceneType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.msg_alter
     *
     * @return the value of t_scene.msg_alter
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public Byte getMsgAlter() {
        return msgAlter;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.msg_alter
     *
     * @param msgAlter the value for t_scene.msg_alter
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setMsgAlter(Byte msgAlter) {
        this.msgAlter = msgAlter;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.last_op_time
     *
     * @return the value of t_scene.last_op_time
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public Date getLastOpTime() {
        return lastOpTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.last_op_time
     *
     * @param lastOpTime the value for t_scene.last_op_time
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.scene_run
     *
     * @return the value of t_scene.scene_run
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public Integer getSceneRun() {
        return sceneRun;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.scene_run
     *
     * @param sceneRun the value for t_scene.scene_run
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setSceneRun(Integer sceneRun) {
        this.sceneRun = sceneRun;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.license
     *
     * @return the value of t_scene.license
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public Integer getLicense() {
        return license;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.license
     *
     * @param license the value for t_scene.license
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setLicense(Integer license) {
        this.license = license;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.alter_need
     *
     * @return the value of t_scene.alter_need
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public Integer getAlterNeed() {
        return alterNeed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.alter_need
     *
     * @param alterNeed the value for t_scene.alter_need
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setAlterNeed(Integer alterNeed) {
        this.alterNeed = alterNeed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_scene.scene_group
     *
     * @return the value of t_scene.scene_group
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public String getSceneGroup() {
        return sceneGroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_scene.scene_group
     *
     * @param sceneGroup the value for t_scene.scene_group
     *
     * @mbg.generated Fri Mar 02 10:21:07 CST 2018
     */
    public void setSceneGroup(String sceneGroup) {
        this.sceneGroup = sceneGroup == null ? null : sceneGroup.trim();
    }

	/**  
	 * @param sceneNumber
	 * @param sceneName
	 * @param oboxSerialId
	 * @param oboxSceneNumber
	 * @param sceneStatus
	 * @param sceneType
	 * @param msgAlter
	 * @param lastOpTime
	 * @param sceneRun
	 * @param license
	 * @param alterNeed
	 * @param sceneGroup  
	 */
	public TScene(Integer sceneNumber, String sceneName, String oboxSerialId, Integer oboxSceneNumber, Integer sceneStatus,
			String sceneType, Byte msgAlter, Date lastOpTime, Integer sceneRun, Integer license, Integer alterNeed,
			String sceneGroup) {
		super();
		this.sceneNumber = sceneNumber;
		this.sceneName = sceneName;
		this.oboxSerialId = oboxSerialId;
		this.oboxSceneNumber = oboxSceneNumber;
		this.sceneStatus = sceneStatus;
		this.sceneType = sceneType;
		this.msgAlter = msgAlter;
		this.lastOpTime = lastOpTime;
		this.sceneRun = sceneRun;
		this.license = license;
		this.alterNeed = alterNeed;
		this.sceneGroup = sceneGroup;
	}

	/**  
	 *   
	 */
	public TScene() {
		super();
	}

	/**  
	 * @param sceneNumber  
	 */
	public TScene(Integer sceneNumber) {
		super();
		this.sceneNumber = sceneNumber;
	}
    
}