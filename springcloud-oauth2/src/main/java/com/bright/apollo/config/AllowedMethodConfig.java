package com.bright.apollo.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;

/**  
 *@Title:  
 *@Description:  allow for auth/token
 *@Author:JettyLiu
 *@Since:2018年3月7日  
 *@Version:1.1.0  
 */
//@Configuration
public class AllowedMethodConfig {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @PostConstruct
    public void reconfigure() {
        Set<HttpMethod> allowedMethods =
            new HashSet<HttpMethod>(Arrays.asList(HttpMethod.GET, HttpMethod.POST));
        tokenEndpoint.setAllowedRequestMethods(allowedMethods);
    }

}
