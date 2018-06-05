package com.bright.apollo.enums;

public enum ErrorEnum {
	
	user_expire_right("1001"),
	
	access_token_invalid("1002"),
	
	user_not_found("1003"),
	
	user_login_fail("1004"),
	
	user_login_param_invalid("1005"),
	
	user_name_exist("1006"),
	
	request_fail_not_online("2002"),
	
	request_send_fail("2003"),
	
	request_send_timeout_fail("2004"),
	
	request_send_unknow_fail("2005"),
	
	request_send_reply_fail("2006"),
	
	request_send_noGateway_fail("2007"),
	
	request_ing_fail("2008"),
	
	request_upgrade_lastestVer_fail("2009"),
	
	request_target_null("2010"),
	
	request_param_invalid("2001"),
	
	ys_not_operative("3001"),
	
	ys_accessToken_fail("3002"),
	
	default_is_uncover("3003"),
	
	equipment_inexistence("3004"),
	
	sms_verify_fail("4001"),
	
	nvr_connect_fail("5001"),
	
	no_device_exist("7001"),
	
	the_object_exist("7007"),
	
	
	the_device_type_exist("7003"),
	
	the_brand_device_type_exist("7005"),
	
	the_brand_exist("7004"),
	
	the_device_no_exist("7006"),
	
	the_bind_device_exist("7007"),
	
	the_bind_device_no_exist("7008"),
	
	app_match_code_null("8000")
	;
	
	private String value;
	
	private ErrorEnum(String v) {
		this.value = v;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	
	}
}
