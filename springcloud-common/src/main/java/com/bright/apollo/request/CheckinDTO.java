package com.bright.apollo.request;

import java.io.Serializable;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月5日  
 *@Version:1.1.0  
 */
public class CheckinDTO implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 2296423708382145094L;

	
	private String mobile;
	
	
	private Long openTime;
	
	
	private Long closeTime;
	
	
	private HotelDTO hotel;
	
	
	private RomeDTO rome;
	
	
	private Boolean tempPwd;
}
