package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.dao.oauth.mapper.OauthMapper;
import com.bright.apollo.service.OauthService;
@Service
public class OauthServiceImpl implements OauthService {
	@Autowired
    private OauthMapper oauthMapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.OauthService#addDevice(com.bright.apollo.common.entity.OauthClientDetails)  
	 */
	@Override
	public void addOauthClientDetails(OauthClientDetails oauthClientDetails) {
		   
		oauthMapper.addOauthClientDetails(oauthClientDetails);
	}
	
}
