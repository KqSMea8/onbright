package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.dao.user.mapper.TUserOboxMapper;
import com.bright.apollo.service.UserOboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserOboxServiceImpl implements UserOboxService {

    @Autowired
    private TUserOboxMapper userOboxMapper;

    @Override
    public List<TUserObox> getUserOboxBySerialId(String oboxSerialId) {
        return userOboxMapper.getUserOboxBySerialId(oboxSerialId);
    }

    @Override
    public void addUserObox(TUserObox tUserObox) {
        userOboxMapper.addUserObox(tUserObox);
    }

    @Override
    public void delectUserOboxByOboxSerialId(String oboxSerialId) {
    	userOboxMapper.delectUserOboxByOboxSerialId(oboxSerialId);
    }

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserOboxService#getUserOboxByUserIdAndOboxSerialId(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public TUserObox getUserOboxByUserIdAndOboxSerialId(Integer userId, String oboxSerialId) {
		 
		return userOboxMapper.getUserOboxByUserIdAndOboxSerialId(userId,oboxSerialId);
	}

	 
}
