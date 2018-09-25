package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.OauthClientDetails;

public interface OauthService {
	void addOauthClientDetails(OauthClientDetails oauthClientDetails);

	/**  
	 * @param grantType
	 * @return  
	 * @Description:  
	 */
	List<OauthClientDetails> getClients(String grantType);

	/**  
	 * @param clientId
	 * @return  
	 * @Description:  
	 */
	OauthClientDetails queryClientByClientId(String clientId);
}
