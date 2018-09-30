package com.bright.apollo.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
   
    //should design the tables
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	TUser tUser =null;
    	if(NumberHelper.isNumeric(username)){
    		tUser =userService.queryUserByName(username);
    	}else{
    		tUser =userService.queryUserByOpenId(username);
    	}
    	if (tUser == null) {
            throw new UsernameNotFoundException("用户:" + username + ",不存在!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        boolean enabled = true;  
        boolean accountNonExpired = true; 
        boolean credentialsNonExpired = true; 
        boolean accountNonLocked = true;  
        
        User user = new User(username, tUser.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
        return user;
    }

}