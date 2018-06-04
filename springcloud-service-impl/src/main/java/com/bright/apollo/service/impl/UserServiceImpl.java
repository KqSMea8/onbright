package com.bright.apollo.service.impl;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import com.bright.apollo.dao.user.mapper.TUserDeviceMapper;
import com.bright.apollo.dao.user.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.bright.apollo.business.UserBusiness;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserDeviceExample;
import com.bright.apollo.common.entity.TUserExample;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserOboxExample;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.common.entity.TUserSceneExample;
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

	@Override
	public <T, E> T handlerExample(E e) {
		List<T> selectByExample = handlerExampleToList(e);
		if (selectByExample != null && selectByExample.size() > 0)
			return selectByExample.get(0);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#handlerExampleToList(com.
	 * bright.apollo.business.base.BasicBusiness, java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public <T, E> List<T> handlerExampleToList(E e) {
//		return userBusiness.selectByExample(e);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#queryTById(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T queryTById(T t) {
//		if (t != null)
//			return (T) userBusiness.selectByPrimaryKey(t);
		return null;
	}

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
//		return userBusiness.updateByPrimaryKeySelective(tUser);
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.UserService#deleteUserById(com.bright.apollo.
	 * common.entity.TUser)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int deleteUserById(Integer userId) {
		TUserExample example = new TUserExample();
		example.or().andIdEqualTo(userId);
//		return userBusiness.deleteByExample(example);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOfUserObox(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TUserObox> getListOfUserObox(Integer WxUserId, Integer MobileUserId) {
		  
//		return userBusiness.getListOfUserObox(WxUserId,MobileUserId);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#insertBatch(java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int insertBatchByUserObox(List<TUserObox> tUserOboxs) {
		  
//		return userBusiness.insertBatchByUserObox(tUserOboxs);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOverPrivilegeOfUserObox(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TUserObox> getListOverPrivilegeOfUserObox(Integer WxUserId, Integer MobileUserId) {
		  
//		return userBusiness.getListOverPrivilegeOfUserObox(WxUserId,MobileUserId);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#updateBatch(java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int updateBatchByUserObox(List<TUserObox> list) {
		  
//		return userBusiness.updateBatchByUserObox(list);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOfUserDevice(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TUserDevice> getListOfUserDevice(Integer WxUserId, Integer MobileUserId) {
		  
//		return userBusiness.getListOfUserDevice(WxUserId,MobileUserId);
		return  null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#insertBatchByUserDevice(java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int insertBatchByUserDevice(List<TUserDevice> tUserDevices) {
//		return userBusiness.insertBatchByUserDevice(tUserDevices);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#getListOverPrivilegeOfUserDevice(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TUserDevice> getListOverPrivilegeOfUserDevice(Integer WxUserId, Integer MobileUserId) {
	 
//		return userBusiness.getListOverPrivilegeOfUserDevice(WxUserId,MobileUserId);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#updateBatchByUserDevice(java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int updateBatchByUserDevice(List<TUserDevice> list) {
//		return userBusiness.updateBatchByUserDevice(list);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#queryUserOboxByUserId(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TUserObox> queryUserOboxByUserId(Integer id) {
		TUserOboxExample example=new TUserOboxExample();
		example.or().andUserIdEqualTo(id);
		example.setOrderByClause("order by last_op_time desc");
//		return userBusiness.selectByExample(example);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#queryUserDeviceByUserId(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TUserDevice> queryUserDeviceByUserId(Integer id) {

		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserService#queryUserSceneByUserId(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TUserScene> queryUserSceneByUserId(Integer id) {
		TUserSceneExample example=new TUserSceneExample();
		example.or().andUserIdEqualTo(id);
		example.setOrderByClause("order by last_op_time desc");
//		return userBusiness.selectByExample(example);
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


}
