package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum CMDEnum {
	test,
	
	//ir
	regist_aliDev,
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
	query_ir_testcode,//手动匹配遥控方案
	
	query_ir_device_type, //获取遥控云遥控类型

	query_ir_brand, //获取遥控云品牌类型

	bind_ir_remotecode, //手动匹配/一键匹配遥控方案——绑定码库方案

	query_bind_remote_control, //获取绑定红外

	modify_ir_program,//修改红外转发器方案

	control_ir_device,//控制转发命令

	delete_ir_device,//删除红外遥控方案

	rename_ir_device,//重命名红外遥控方案

	delete_ir_device_key,//删除方案中特定按键

	pair_ir_remotecode,//一键匹配遥控方案——进入空调对码模式

	create_ir_device,//学习遥控方案——新建自定义遥控器

	learn_ir_device_key,//学习遥控方案——进入按键学习模式

	query_ir_device,//获取红外遥控方案

	to_key_learn,//进入学习状态

	to_pair_code,//进入对码模式

	send_test_code,//发送测试码

	control_device,//控制(红外)

	setting_security_scene,

	local_ir_device_download,//本地遥控方案——下载方案

	local_ir_device_delete,//本地遥控方案——删除方案

	query_groupid_by_addr,
	
	query_scenenumber_by_addr, 
	
	query_security_scene, 
	
	addOrUpdate_timing_task, 
	
	query_timing_task, 
	
	delete_timing_task, 
	
	delete_security_scene,
	
	app_match,
	
	query_intelligent_fingerHome,
	
	query_intelligent_openRecord,
	
	query_intelligent_warningRecord,
	
	query_intelligent_useringRecord,
	
	edit_intelligent_user,
	
	send_intelligent_validateCode,
	
 
	
	add_intelligent_authPwd,
	
	query_intelligent_authPwd,
	
	add_intelligent_remote_user,
	
	query_intelligent_remote_unLocking,
	
	add_Intelligent_Remote_UnLocking,
	
	del_intelligent_remote_user,
	
	forget_intelligent_pwd,
	
	reset_intelligent_pwd,
	
	set_aircraft_status("8020", "a020"), 
	
	set_aircraft_group("8006", "a006"),
	
	//设置门锁远程用户
	set_finger_remote_user("8102","a102"), 
	
	respone_finger_remote_user("0503","2503"),
	
	query_intelligent_push_list, 
	
	modify_intelligent_remote_user, 
	
	send_remote_pwd, 
	
	reset_intelligent_pwd_by_code, 
	
	modify_intelligent_push, 
	
	del_all_intelligent_remote_user,

	query_ali_dev,

	set_ali_dev,

	read_ali_dev,

	query_timer,

	set_timer,

	upload_config,

	set_countdown,

	delete_ali_dev,

	query_countdown,

	setting_remote_led("8103","a103"),
	my_test, 
	
	modify_location, 
	
	delete_location, 
	
	delete_scene_location, 
	
	query_remote_led_name, 
	
	setting_remote_led_name, delete_device_location;
		
	
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
