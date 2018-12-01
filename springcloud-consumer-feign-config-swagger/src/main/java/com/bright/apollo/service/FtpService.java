package com.bright.apollo.service;

import java.io.InputStream;

import com.bright.apollo.vo.PicPathVo;
import com.netflix.infix.lang.infix.antlr.EventFilterParser.boolean_expr_return;


/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月27日  
 *@Version:1.1.0  
 */
public interface FtpService {
	public String[] uploadFile(String originFileName,String oldPath,PicPathVo pathVo);
	public String saveToTemp(String originFileName,InputStream input,PicPathVo pathVo);
	public boolean deleteFtpFile(String path,PicPathVo pathVo);
	//public String uploadZipFile(String originFileName,InputStream input,PicPathVo pathVo);
}
