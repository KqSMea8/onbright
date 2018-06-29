package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.dao.device.mapper.TYSCameraMapper;
import com.bright.apollo.service.CameraService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月28日  
 *@Version:1.1.0  
 */
@Service
public class CameraServiceImpl implements CameraService{
	@Autowired
	private TYSCameraMapper tysCameraMapper;
 
	/**  
	 *   
	 */
	public CameraServiceImpl() {
		super();  
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.CameraService#getYSCameraBySerialId(java.lang.String)  
	 */
	@Override
	public TYSCamera getYSCameraBySerialId(String deviceSerialId) {
		  
		return tysCameraMapper.getYSCameraBySerialId(deviceSerialId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.CameraService#getYSCameraByUserId(java.lang.Integer)  
	 */
	@Override
	public List<TYSCamera> getYSCameraByUserId(Integer userId) {
 		return tysCameraMapper.getYSCameraByUserId(userId);
	}

}
