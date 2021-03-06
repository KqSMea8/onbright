package com.bright.apollo.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TServerGroup implements Serializable{
    /**  
	 *   
	 */
	private static final long serialVersionUID = 8573887303051744670L;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_server_group.id
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
	@JsonProperty(value="group_id")
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_server_group.group_name
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    @JsonProperty(value="group_name")
    private String groupName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_server_group.last_op_time
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    private Date lastOpTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_server_group.group_state
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    @JsonProperty(value="group_state")
    private String groupState;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_server_group.group_type
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    @JsonProperty(value="group_type")
    private String groupType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_server_group.group_child_type
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    @JsonProperty(value="group_child_type")
    private String groupChildType;
 

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_server_group.group_addr
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    @JsonProperty(value="groupAddr")
    private String groupAddr;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_server_group.group_style
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    @JsonProperty(value="group_style")
    private String groupStyle;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_server_group.id
     *
     * @return the value of t_server_group.id
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_server_group.id
     *
     * @param id the value for t_server_group.id
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_server_group.group_name
     *
     * @return the value of t_server_group.group_name
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_server_group.group_name
     *
     * @param groupName the value for t_server_group.group_name
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_server_group.last_op_time
     *
     * @return the value of t_server_group.last_op_time
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public Date getLastOpTime() {
        return lastOpTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_server_group.last_op_time
     *
     * @param lastOpTime the value for t_server_group.last_op_time
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_server_group.group_state
     *
     * @return the value of t_server_group.group_state
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public String getGroupState() {
        return groupState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_server_group.group_state
     *
     * @param groupState the value for t_server_group.group_state
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public void setGroupState(String groupState) {
        this.groupState = groupState == null ? null : groupState.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_server_group.group_type
     *
     * @return the value of t_server_group.group_type
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_server_group.group_type
     *
     * @param groupType the value for t_server_group.group_type
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType == null ? null : groupType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_server_group.group_child_type
     *
     * @return the value of t_server_group.group_child_type
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public String getGroupChildType() {
        return groupChildType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_server_group.group_child_type
     *
     * @param groupChildType the value for t_server_group.group_child_type
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public void setGroupChildType(String groupChildType) {
        this.groupChildType = groupChildType == null ? null : groupChildType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_server_group.group_addr
     *
     * @return the value of t_server_group.group_addr
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public String getGroupAddr() {
        return groupAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_server_group.group_addr
     *
     * @param groupAddr the value for t_server_group.group_addr
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public void setGroupAddr(String groupAddr) {
        this.groupAddr = groupAddr == null ? null : groupAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_server_group.group_style
     *
     * @return the value of t_server_group.group_style
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public String getGroupStyle() {
        return groupStyle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_server_group.group_style
     *
     * @param groupStyle the value for t_server_group.group_style
     *
     * @mbg.generated Mon Oct 29 16:21:22 CST 2018
     */
    public void setGroupStyle(String groupStyle) {
        this.groupStyle = groupStyle == null ? null : groupStyle.trim();
    }

	@Override
	public String toString() {
		return "TServerGroup [id=" + id + ", groupName=" + groupName + ", lastOpTime=" + lastOpTime + ", groupState="
				+ groupState + ", groupType=" + groupType + ", groupChildType=" + groupChildType + ", groupAddr="
				+ groupAddr + ", groupStyle=" + groupStyle + "]";
	}
    
    
}