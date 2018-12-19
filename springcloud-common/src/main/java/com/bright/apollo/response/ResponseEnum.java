package com.bright.apollo.response;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月9日  
 *@Version:1.1.0  
 */
public enum ResponseEnum {
	AddSuccess(201,"add success"),
	UpdateSuccess(205,"update success"),
	DeleteSuccess(204,"delete success"),
	SelectSuccess(200,"select success"),
	AuthenticationSuccess(200,"Authentication success"),
	
	AuthenticationError(401,"Authentication does not pass"),
	NoIRKey(404,"have not key"),
	LearnKeyFailed(405,"to learn failed"),
	PairCodeFailed(406,"pair code failed"),
	ErrorMobile(410,"error moble"),
	ExistMobile(411,"the mobile is exist"),
	NoExistMobile(412,"the mobile is not exist"),
	RequestObjectNotExist(413,"request object not exist"),
	RequestParamError(414,"request param error"),
	WxLoginError(415,"weixin login error"),
	NoExistOpenId(416,"the weixin openId"),
	NoExistCode(417,"the code timeout or code is error"),
	ObjExist(418,"the obj exist"),
	UnKonwUser(419,"User Not Exist"),
	SearchIsEmpty(420,"Search Is Empty"),
	SendOboxError(421,"Send Obox error"),
	SendOboxTimeOut(422,"Send Obox TimeOut"),
	SendOboxFail(423,"Send Obox Fail"),
	SendOboxUnKnowFail(424,"Send Obox UnKnow Fail"),
	REQUESTFAILNOTONLINE(425,"request fail not online"),
	AddObjError(426,"add obj error"),
	MultipleObjExist(427,"multiple  obj exist"),
 
	
	Error(511,"system error"),
 	ServerError(512,"server is error"),
	RequestTimeout(513,"request server timeout"),
	MicroServiceUnConnection(514,"Micro Service UnConnection");
	private int status;
	
	private String msg;
 
	private ResponseEnum(int status, String msg) {
		this.status=status;
		this.msg=msg;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMessage(String msg) {
		this.msg = msg;
	}
	
}
