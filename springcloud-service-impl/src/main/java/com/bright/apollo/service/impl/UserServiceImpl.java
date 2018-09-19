package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.bright.apollo.business.UserBusiness;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserScene;
//import org.springframework.beans.factory.annotation.Autowired;
import com.bright.apollo.dao.user.mapper.TUserDeviceMapper;
import com.bright.apollo.dao.user.mapper.TUserMapper;
import com.bright.apollo.dao.user.mapper.TUserOboxMapper;
import com.bright.apollo.dao.user.mapper.TUserSceneMapper;
import com.bright.apollo.service.UserService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月9日
 * @Version:1.1.0
 */
@Service
public class UserServiceImpl implements UserService {
//	@SuppressWarnings({ "rawtypes" })
//	@Autowired
//	private UserBusiness userBusiness;

	/* 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#handlerExample(com.bright.
	 * apollo.business.base.BasicBusiness, java.lang.Object)
	 */
	@Autowired
	private TUserMapper userMapper;

	@Autowired
	private TUserDeviceMapper userDeviceMapper;

	@Autowired
	private TUserOboxMapper tUserOboxMapper;
	
	@Autowired
	private TUserSceneMapper tUserSceneMapper;
	
	 
	@Override
	public TUser queryUserByName(String name) {
		return	userMapper.getUserByUserName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.UserService#queryUserByOpenId(java.lang.String)
	 */
	@Override
	public TUser queryUserByOpenId(String openId) {
		return userMapper.getUserByOpenId(openId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.UserService#saveUserByWeiXinInfo(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public int saveUserByWeiXinInfo(String openid, String headimgurl, String nickname) {

//		return userBusiness.insertSelective(new TUser(openid, nickname, headimgurl));
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.UserService#updateUser(com.bright.apollo.common
	 * .entity.TUser)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int updateUser(TUser tUser) {
		userMapper.updateUser(tUser);
		return 0;
	}

 

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOfUserObox(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TUserObox> getListOfUserObox(Integer WxUserId, Integer MobileUserId) {
		  
//		return userBusiness.getListOfUserObox(WxUserId,MobileUserId);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#insertBatch(java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public int insertBatchByUserObox(List<TUserObox> tUserOboxs) {
		  
//		return userBusiness.insertBatchByUserObox(tUserOboxs);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOverPrivilegeOfUserObox(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TUserObox> getListOverPrivilegeOfUserObox(Integer WxUserId, Integer MobileUserId) {
		  
//		return userBusiness.getListOverPrivilegeOfUserObox(WxUserId,MobileUserId);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#updateBatch(java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public int updateBatchByUserObox(List<TUserObox> list) {
		  
//		return userBusiness.updateBatchByUserObox(list);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOfUserDevice(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TUserDevice> getListOfUserDevice(Integer WxUserId, Integer MobileUserId) {
		  
//		return userBusiness.getListOfUserDevice(WxUserId,MobileUserId);
		return  null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#insertBatchByUserDevice(java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public int insertBatchByUserDevice(List<TUserDevice> tUserDevices) {
//		return userBusiness.insertBatchByUserDevice(tUserDevices);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOverPrivilegeOfUserDevice(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TUserDevice> getListOverPrivilegeOfUserDevice(Integer WxUserId, Integer MobileUserId) {
	 
//		return userBusiness.getListOverPrivilegeOfUserDevice(WxUserId,MobileUserId);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#updateBatchByUserDevice(java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public int updateBatchByUserDevice(List<TUserDevice> list) {
//		return userBusiness.updateBatchByUserDevice(list);
		return 0;
	}

	 

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#queryUserDeviceByUserId(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TUserDevice> queryUserDeviceByUserId(Integer id) {

		return null;
	}

 

	@Override
	public TUser getUserByUserId(int id) {
		return userMapper.getUserById(id);
	}

	@Override
	public List<TUser> getUsersBySceneNumber(int sceneNumber) {
		return userMapper.getUsersBySceneNumber(sceneNumber);
	}

	@Override
	public List<TUser> getUsersByDeviceId(int configDeviceId) {
		return userDeviceMapper.getUsersByDeviceId(configDeviceId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#addUserObox(com.bright.apollo.common.entity.TUserObox)  
	 */
	@Override
	public int addUserObox(TUserObox tUserObox) {
		  
		return tUserOboxMapper.addUserObox(tUserObox);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#addUserDevice(com.bright.apollo.common.entity.TUserDevice)  
	 */
	@Override
	public int addUserDevice(TUserDevice tUserDevice) {
 		return userDeviceMapper.addUserDevice(tUserDevice);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#addUserScene(com.bright.apollo.common.entity.TUserScene)  
	 */
	@Override
	public void addUserScene(TUserScene tUserScene) {
		  
		tUserSceneMapper.addUserScene(tUserScene);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getUserSceneByUserIdAndSceneNumber(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public TUserScene getUserSceneByUserIdAndSceneNumber(Integer userId, Integer sceneNumber) {
		 return tUserSceneMapper.getUserSceneByUserIdAndSceneNumber(userId,sceneNumber);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOfUserDeviceByUserId(java.lang.Integer)  
	 */
	@Override
	public List<TUserDevice> getListOfUserDeviceByUserId(Integer userId) {
		return userDeviceMapper.getListOfUserDeviceByUserId(userId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#deleteUserById(java.lang.Integer)  
	 */
	@Override
	public int deleteUserById(Integer userId) {
		// TODO Auto-generated method stub  
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#queryUserOboxByUserId(java.lang.Integer)  
	 */
	@Override
	public List<TUserObox> queryUserOboxByUserId(Integer id) {
		// TODO Auto-generated method stub  
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#queryUserSceneByUserId(java.lang.Integer)  
	 */
	@Override
	public List<TUserScene> queryUserSceneByUserId(Integer id) {
		// TODO Auto-generated method stub  
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#addUser(java.lang.String, java.lang.String)  
	 */
	@Override
	public int addUser(String mobile, String pwd) {
 		return userMapper.addUser(mobile,pwd);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#queryUserBySceneNumber(java.lang.Integer)  
	 */
	@Override
	public List<TUser> queryUserBySceneNumber(Integer sceneNumber) {
 		return userMapper.queryUserBySceneNumber(sceneNumber);
	}
 


}
