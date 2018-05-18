package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum CMDEnum {
    //ir

    query_ir_cfg,
    //查询门锁操作日志
    query_finger_log,

    query_device_status,

    query_finger_home,

    add_fingerprint,

    add_user_finger,

    query_fingerprint,

    query_fingerprint_user,

    add_nvr,

    query_nvr_config,

    register,

    set_pwd,

    pwd_forget,

    modify_user,

    msg_alter,

    msg_alter_apply,

    query_user,

    bind_device,

    login,

    /**
     * add obox
     */
    add_obox,

    regist_aliDev,

    upload_config,

    query_ali_dev,

    set_ali_dev,

    read_ali_dev,

    set_timer,

    set_countdown,

    query_timer,

    query_countdown,

    /*
     * query at DB and bind
     */
    query_obox_bind,

    query_group,

    query_obox,

    query_obox_status,

    query_obox_config,

    query_device,

    search_device,

    rename_device,

    query_device_count,

    query_device_status_history,

    get_status,

    replace_config,

    set_device_location,

    set_scene_location,

    query_device_location,

    query_scene_location,

    query_location,

    create_location,

    set_obox_location,

    query_obox_location_history,

    query_location_status_history,

    query_latest_location_status_history,

    query_user_operation_history,

    query_camera,

    query_camera_addr,

    bind_ys_user,

    create_ys_camera,

    set_camera_ptz,

    set_camera_capture,

    query_camera_capture,

    query_ys_access_token,

    create_hotel,

    bind_hotel_obox,

    bind_user_license,

    query_hotel_info,

    query_hotel_obox,

    query_user_device,

    set_drone_status("8100", "a100"),

    get_real_status("0100", "2100"),


    /**
     * update obox information
     */
//	update_obox_scene,

    time("8013", "a013"),


    /**
     * delete obox information
     */
    delete_obox("8012", "a012"),

    /**
     * control obox
     */
//	control_obox,

    /**
     * execute obox scene
     */
//	execute_box_scene,

    /**
     * update obox pwd
     */
    update_obox_pwd("8007", "a007"),

    /*
     * update obox info
     */
    update_obox_info("8001", "a001"),

    /**
     * 重置密码
     */
    reset_obox_pwd("800b", "a00b"),

    /**
     * update obox name
     */
//	update_box_name,

    /**
     * 搜索新节点
     */
    search_new_device("0003","2003"),

    getting_new_device("0003","2003"),

    modify_device("8004", "a004"),

    set_group("8006", "a006"),


    detect_remoter("8014", "a014"),

    set_remoter("8016", "a016"),

    query_remoter,

    query_remoter_channel,

    register_remoter,
    //更新异常为已读状态
    update_msg_state,

    query_msg,

    query_version("000c", "200c"),

    update_node_name("8004", "a004"),


    setting_node_status("8100", "a100"),


    /**
     * 设置场景ID相关信息
     */
    setting_sc_id("800e", "a00e"),

    /**
     * 设置场景条件相关信息
     */
    setting_sc_condition("800e", "a00e"),

    /**
     * 设置场景行为相关信息
     */
    setting_sc_action("800e", "a00e"),

    setting_sc_info("800e", "a00e"),

    execute_sc("800e", "a00e"),

    /**
     * 查询所有场景信息
     */
    query_scenes(),

    /**
     * 查询设备更新
     */
    query_upgrades(),

    /**
     * 释放所有节点
     */
    release_all_devices("8008", "a008"),

    setting_channel("8015","a015"),

    setting_upgrade("8010","a010"),

    sending_upgrade("a1","b1"),

    upgrade_progress("a4","b4"),

    status_update("0500",""),

    drone_update("0501",""),
    //获取实时状态
    //get_rt_status("35",""),

    camera_heart_beat("c1","41"),

    query_node_real_status("0100", "2100"),
    //遥控云
    query_remote_control,

    register_device,

    query_device_type,

    query_brand,

    query_remote_control_id,

    query_remote_control_src,

    bind_remote_control,

    query_bind_remote_control,

    setting_security_scene,

    setting_timing_task,

    query_groupid_by_addr,

    app_match,

    IRDN("98","irDn"),//IR下载码库

    query_key_code,

//	upload_key_code_ir,

    download_key_code_ir,

    query_scenenumber_by_addr;


    private String sendCMD;

    private String replyCMD;

    private CMDEnum() {

    }

    private CMDEnum(String sendCMD, String replyCMD) {
        this.sendCMD = sendCMD;
        this.replyCMD = replyCMD;
    }


    public String getSendCMD() {
        return sendCMD;
    }

    public void setSendCMD(String sendCMD) {
        this.sendCMD = sendCMD;
    }

    public String getReplyCMD() {
        return replyCMD;
    }

    public void setReplyCMD(String replyCMD) {
        this.replyCMD = replyCMD;
    }

    public static CMDEnum getCMDEnum(String cmd) {
        for (CMDEnum cmdEnum : CMDEnum.values()) {
            if (cmdEnum.name().equals(cmd)) {
                return cmdEnum;
            }
        }
        return null;
    }

}
