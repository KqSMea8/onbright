package com.bright.apollo.service;

import com.bright.apollo.common.entity.TUserObox;

import java.util.List;

public interface UserOboxService {

    List<TUserObox> getUserOboxBySerialId(String oboxSerialId);

    void addUserObox(TUserObox tUserObox);

    void delectUserOboxByOboxSerialId(String oboxSerialId);

	/**  
	 * @param userId
	 * @param oboxSerialId
	 * @return  
	 * @Description:  
	 */
	TUserObox getUserOboxByUserIdAndOboxSerialId(Integer userId, String oboxSerialId);

 
}
