package com.bright.apollo.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bright.apollo.cache.CacheHelper;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.service.UserService;
import com.bright.apollo.tool.NumberHelper;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月2日  
 *@Version:1.1.0  
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
	private CacheHelper cacheHelper;
    //should design the tables
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	TUser tUser =null;
    	if(NumberHelper.isNumeric(username)){
    		tUser =userService.queryUserByName(username);
    	}else{
    		String openId = cacheHelper.getOpenId(username);
    		tUser =userService.queryUserByOpenId(StringUtils.isEmpty(openId)?username:openId);
    		username=tUser!=null?tUser.getOpenId():username;
    	}
    	if (tUser == null) {
            throw new UsernameNotFoundException("用户:" + username + ",不存在!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        boolean enabled = true;  
        boolean accountNonExpired = true; 
        boolean credentialsNonExpired = true; 
        boolean accountNonLocked = true;  
        
        User user = new User(username, StringUtils.isEmpty(tUser.getPassword())?username:tUser.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
        return user;
    }

}