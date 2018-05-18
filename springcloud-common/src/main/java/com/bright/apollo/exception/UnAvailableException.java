package com.bright.apollo.exception;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月2日  
 *@Version:1.1.0  
 */
public class UnAvailableException extends RuntimeException{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 7083833802465996125L;
	
	public UnAvailableException(){
		super("service UnAvailable Exception");
	}

}
