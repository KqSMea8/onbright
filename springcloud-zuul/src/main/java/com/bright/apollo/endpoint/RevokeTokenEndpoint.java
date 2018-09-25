package com.bright.apollo.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bright.apollo.config.AuthorizationServerConfiguration;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;


/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月21日  
 *@Version:1.1.0  
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;
    private static final Logger logger = LoggerFactory.getLogger(RevokeTokenEndpoint.class);
    @SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token/{access_token}")
    @ResponseBody
    public ResponseObject revokeToken(@PathVariable("access_token")String access_token) {
    	ResponseObject res=new ResponseObject();
    	try {
    		if (consumerTokenServices.revokeToken(access_token)){
    			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
    			res.setMessage("logout success");
            }else{
            	res.setStatus(ResponseEnum.Error.getStatus());
    			res.setMessage("logout failed");
            }
		} catch (Exception e) {
			logger.info("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
    	logger.info("====msg:"+res.getMessage());
        return res;
    }
}