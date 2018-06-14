package com.bright.apollo.service.impl;

import java.util.List;

import com.bright.apollo.dao.obox.mapper.TOboxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxExample;
import com.bright.apollo.service.OboxService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Service
public class OboxServiceImpl implements OboxService {

	@Autowired
	private TOboxMapper oboxMapper;



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#handlerExample(java.lang.
	 * Object)
	 */
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
	 * com.bright.apollo.service.base.BasicService#handlerExampleToList(java.
	 * lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T, E> List<T> handlerExampleToList(E e) {
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
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxService#update(com.bright.apollo.common.entity.TObox)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(TObox obox) {
		oboxMapper.updateObox(obox);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxService#deleteOboxById(com.bright.apollo.common.entity.TObox)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteOboxById(TObox obox) {
		oboxMapper.deleteOboxById(obox);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxService#queryOboxBySerialId(java.lang.String)  
	 */
	@Override
	public TObox queryOboxBySerialId(String oboxSerialId) {
		TOboxExample example=new TOboxExample();
		example.or().andOboxSerialIdEqualTo(oboxSerialId);
		return handlerExample(example);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxService#addObox(com.bright.apollo.common.entity.TObox)  
	 */
 	@Override
	public int addObox(TObox obox) {
		  return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxService#queryOboxByUserId(java.lang.Integer, java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TObox> queryOboxByUserId(Integer userId, Integer pageIndex, Integer pageSize) {
		  
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OboxService#queryCountOboxByUserId(java.lang.Integer)  
	 */
	@Override
	public int queryCountOboxByUserId(Integer userId) {
		 
		return 0;
	}

	@Override
	public TObox queryOboxsByOboxSerialId(String oboxSerialId) throws Exception {
		return oboxMapper.queryOboxsByOboxSerialId(oboxSerialId);
	}

	@Override
	public TObox queryOboxsByDeviceChannelId(int deviceId) throws Exception {
		return oboxMapper.queryOboxsByDeviceChannelId(deviceId);
	}

	@Override
	public TObox queryOboxById(int id) throws Exception {
		return oboxMapper.queryOboxsById(id);
	}

	@Override
	public List<TObox> getOboxsByDeviceChannel(int oboxDeviceId, int oboxId) {
		return oboxMapper.getOboxsByDeviceChannel(oboxDeviceId,oboxId);
	}

}
