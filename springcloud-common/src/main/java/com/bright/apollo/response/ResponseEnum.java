package com.bright.apollo.response;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月9日  
 *@Version:1.1.0  
 */
public enum ResponseEnum {
	
	ErrorMobile(201,"error moble"),
	ExistMobile(202,"the mobile is exist"),
	Error(203,"system error"),
	NoExistMobile(204,"the mobile is not exist"),
	ServerError(205,"server is error"),
	RequestTimeout(206,"request server timeout"),
	RequestObjectNotExist(207,"request object not exist"),
	RequestParamError(208,"request param error"),
	WxLoginError(209,"weixin login error"),
	NoExistOpenId(210,"the weixin openId"),
	NoExistCode(211,"the code timeout or code is error"),
	ObjExist(212,"the obj exist"),
	MicroServiceUnConnection(213,"Micro Service UnConnection"),
	UnKonwUser(214,"User Not Exist"),
	SearchIsEmpty(215,"Search Is Empty"),
	SendOboxError(216,"Send Obox error"),
	SendOboxTimeOut(217,"Send Obox TimeOut"),
	SendOboxFail(218,"Send Obox Fail"),
	SendOboxUnKnowFail(219,"Send Obox UnKnow Fail"),
	Success(200,"success");
	private int code;
	
	private String msg;
 
	private ResponseEnum(int code, String msg) {
		this.code=code;
		this.msg=msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
